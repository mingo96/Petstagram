package com.example.petstagram.ViewModels

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Notification
import com.example.petstagram.UiData.Pet
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.TypeOfNotification
import com.example.petstagram.UiData.UIPost
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit

fun getMimeType(context: Context, uri: Uri): String? {
    val contentResolver: ContentResolver = context.contentResolver
    return contentResolver.getType(uri)
}

class PublishViewModel : ViewModel() {

    lateinit var base: DataFetchViewModel

    val newPost by mutableStateOf(UIPost())

    private var _isSendingInfo = MutableLiveData(false)

    val isSendingInfo: LiveData<Boolean> = _isSendingInfo

    /**Firebase Storage reference, since it will only push, doesnt need a route*/
    private var storageRef = Firebase.storage.reference

    /**[Profile] of the user posting*/
    var user: Profile = Profile()
        set(value) {
            newPost.creatorUser = value
            field = value
        }

    /**[Category] for the [Post]*/
    var category: Category? = null

    /**Firebase Reference*/
    private val db = Firebase.firestore

    /**Title the [Post] will have*/
    private var postTitle by mutableStateOf("")

    /**[Uri] the resource of the [Post] will have*/
    private var _resource = MutableLiveData(Uri.EMPTY)

    /**[LiveData] for [_resource]*/
    var resource: LiveData<Uri> = _resource

    var petsDisplayed by mutableStateOf(false)
        private set

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())

    val pets: StateFlow<List<Pet>> = _pets

    var selectedPet: Pet? = null
        private set

    private var _text = MutableStateFlow("Enviando")

    val text: StateFlow<String> = _text

    private var ignoredPet = false

    var total: Long by mutableLongStateOf(1L)
    var actual: Long by mutableLongStateOf(1L)

    private var start: Date? by mutableStateOf(null)

    var estimated: String by mutableStateOf("calculando")


    /**changes [postTitle]
     * @param input new value for [postTitle]*/
    fun changeTitle(input: String) {
        newPost.title = input
        postTitle = input
    }

    /**gives the info in [postTitle]
     * @return the actual value of [postTitle]*/
    fun getTitle(): String {
        return postTitle
    }


    /**posts a [Post] with the info we have (if it is valid)*/
    fun postPost(onSuccess: () -> Unit, context: Context) {
        if (!ignoredPet && newPost.pet.isBlank()) {
            togglePetsVisibility()
            Toast.makeText(
                context, "No has seleccionado ninguna mascota, estás seguro?", Toast.LENGTH_SHORT
            ).show()
            ignoredPet = true
            return
        }
        //gotta make sure we dont get any document or strange file, atleast one has to be true

        val isVideo = getMimeType(context = context, _resource.value!!)?.startsWith("video/")
        val isImage = getMimeType(context = context, _resource.value!!)?.startsWith("image/")

        if (isVideo == true || isImage == true) {
            if (resource.value != null && resource.value != Uri.EMPTY && postTitle != "Titulo Publicacion") {

                //creates a post with given info
                newPost.title = postTitle
                newPost.category = category
                newPost.typeOfMedia = if (isImage == true) "image" else "video"
                newPost.creatorUser = user

                persistPost(onSuccess)
            }
        } else {
            Log.i("intento de seleccion no valido", "$isImage, $isVideo")
            Toast.makeText(context, "tipo de archivo no válido", Toast.LENGTH_SHORT).show()
        }

        viewModelScope.launch {
            _text.collect {
                _text.value = when (it) {
                    "Enviando" -> "Enviando."
                    "Enviando." -> "Enviando.."
                    "Enviando.." -> "Enviando..."
                    else -> "Enviando"
                }
                delay(500)
                if (isSendingInfo.value == false) {
                    _text.value = it
                }
            }
        }
    }

    /**sends the created [Post] to the [db]
     * @param post the post about to be persisted
     * @param onSuccess code given for execution once we're finished*/
    private fun persistPost(onSuccess: () -> Unit) {


        viewModelScope.launch {

            _isSendingInfo.value = true
            db.collection("Posts").add(newPost.basePost()).addOnSuccessListener { doc ->
                pushResource(doc.id).addOnProgressListener {
                    if (start == null) {
                        start = Date.from(Instant.now())
                    }
                    actual = it.bytesTransferred
                    total = it.totalByteCount
                    val timeElapsed =
                        TimeUnit.MILLISECONDS.toSeconds(Date.from(Instant.now()).time - start!!.time)
                    val total = (1 * timeElapsed / progress()).toInt() - timeElapsed
                    estimated = total.parseToTime()


                }.addOnSuccessListener {
                    db.collection("Posts").document(doc.id).update("id", doc.id)

                    storageRef.child("PostImages/${doc.id}").downloadUrl.addOnSuccessListener {
                        db.collection("Posts").document(doc.id).update("source", it.toString())
                        newPost.id = doc.id
                        postTitle = "Titulo Publicacion"
                        _resource.value = Uri.EMPTY
                        _isSendingInfo.value = false
                        start = null
                        notifyFollowers()
                        onSuccess()
                    }

                }
            }
        }

    }

    private fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    }

    private fun notifyFollowers() {
        val newNotification = Notification(
            sender = user.id,
            userName = user.userName,
            type = TypeOfNotification.NewPost,
            notificationText = newPost.id +"="+postTitle
        )
        user.followers.forEach { follower ->
            db.collection("NotificationsChannels").whereEqualTo("user", follower).get()
                .addOnSuccessListener {
                    db.collection("NotificationsChannels").document(it.documents[0].id)
                        .update("notifications", FieldValue.arrayUnion(newNotification))

                }

        }

        if (newPost.uiPet!= null) newNotification.userName = newPost.uiPet!!.name
        newPost.uiPet?.followers?.forEach { follower ->
            if (follower !in user.followers) {
                db.collection("NotificationsChannels").whereEqualTo("user", follower).get()
                    .addOnSuccessListener {
                        db.collection("NotificationsChannels").document(it.documents[0].id)
                            .update("notifications", FieldValue.arrayUnion(newNotification))

                    }
            }

        }
    }

    private fun Long.parseToTime(): String {

        val hours = TimeUnit.SECONDS.toHours(this)
        val minutes = TimeUnit.SECONDS.toMinutes(this) % 60
        val seconds = this % 60
        var spare = ""
        if (hours >= 1) spare += "$hours hora" + if (hours > 1) "s " else ""

        if (minutes >= 1) spare += "$minutes minuto" + if (minutes > 1) "s " else ""

        try {

            if (seconds >= 1) spare += "$seconds segundo" + if (seconds > 1) "s" else "" else spare =
                spare.substring(0, spare.length - 1)
        } catch (e: Exception) {
        }
        return spare
    }

    /**simply sends the file in [_resource]
     * @param id the name the file will have (same as post)*/
    private fun pushResource(id: String): UploadTask {
        return storageRef.child("PostImages/$id").putFile(_resource.value!!)
    }

    /**setter for resource*/
    fun setResource(uri: Uri, context: Context) {
        newPost.source = uri.toString()
        val type = getMimeType(context, uri)
        if (type != null) {
            newPost.typeOfMedia = type.substring(0, 5)
        }
        _resource.value = uri
    }

    fun togglePetsVisibility() {
        petsDisplayed = !petsDisplayed
        if (petsDisplayed) viewModelScope.launch {

            _pets.collect {
                base.petsFromUser(user.id)
                delay(1000)
                _pets.value = base.petsFromUser(user.id).ofThisCategory()
            }
        }
        else {
            _pets.value = emptyList()
        }
    }

    fun selectPet(pet: Pet) {
        if (pet != selectedPet) {
            selectedPet = pet
            newPost.pet = pet.id
            newPost.uiPet = pet
        } else {
            selectedPet = null
            newPost.pet = ""

            newPost.uiPet = null
        }
    }

    private fun List<Pet>.ofThisCategory(): List<Pet> {
        return this.filter { category == null || it.category!!.name == (category?.name ?: "") }
    }

    fun progress(): Float {
        val result = actual.toFloat() / (total.toFloat() + 1)
        return result
    }

}
