package com.example.petstagram.ViewModels

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIPost
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

fun getMimeType(context: Context, uri: Uri): String? {
    val contentResolver: ContentResolver = context.contentResolver
    return contentResolver.getType(uri)
}

class PublishViewModel : ViewModel(){


    val newPost by mutableStateOf(UIPost())

    private var _isSendingInfo = MutableLiveData(false)

    val isSendingInfo : LiveData<Boolean> = _isSendingInfo

    /**Firebase Storage reference, since it will only push, doesnt need a route*/
    private var storageRef = Firebase.storage.reference

    /**[Profile] of the user posting*/
    var user : Profile = Profile()
        set(value) {
            newPost.creatorUser = value
            field = value
        }

    /**[Category] for the [Post]*/
    lateinit var category : Category

    /**Firebase Reference*/
    private val db = Firebase.firestore

    /**Title the [Post] will have*/
    private var postTitle by mutableStateOf("Titulo Publicación")

    /**[Uri] the resource of the [Post] will have*/
    private var _resource = MutableLiveData(Uri.EMPTY)

    /**[LiveData] for [_resource]*/
    var resource :LiveData<Uri> =_resource

    private var _text = MutableStateFlow("Enviando")

    val text : StateFlow<String> = _text

    /**changes [postTitle]
     * @param input new value for [postTitle]*/
    fun changeTitle(input:String){
        postTitle = input
    }

    /**gives the info in [postTitle]
     * @return the actual value of [postTitle]*/
    fun getTitle(): String {
        return postTitle
    }


    /**posts a [Post] with the info we have (if it is valid)*/
    fun postPost(onSuccess : ()->Unit, context : Context){
        //gotta make sure we dont get any document or strange file, atleast one has to be true

        val isVideo = getMimeType(context = context,_resource.value!!)?.startsWith("video/")
        val isImage = getMimeType(context = context,_resource.value!!)?.startsWith("image/")

        if(isVideo == true || isImage == true) {
            if (resource.value != null &&
                resource.value != Uri.EMPTY &&
                postTitle != "Titulo Publicacion"
            ) {

                //creates a post with given info
                newPost.title = postTitle
                newPost.category = category
                newPost.typeOfMedia = if (isImage == true) "image" else "video"
                newPost.creatorUser = user

                persistPost(newPost, onSuccess)
            }
        }
        else {
                Log.i("intento de seleccion no valido", "$isImage, $isVideo")
                Toast.makeText(context, "tipo de archivo no válido", Toast.LENGTH_SHORT).show()
            }

        viewModelScope.launch {
            _text.collect{
                _text.value =
                    when(it){
                        "Enviando"->"Enviando."
                        "Enviando."->"Enviando.."
                        "Enviando.."->"Enviando..."
                        else-> "Enviando"
                    }
                delay(500)
                if (isSendingInfo.value == false){
                    _text.value=it
                }
            }
        }
    }

    /**sends the created [Post] to the [db]
     * @param post the post about to be persisted
     * @param onSuccess code given for execution once we're finished*/
    private fun persistPost(post: Post, onSuccess: () -> Unit){

        _isSendingInfo.value = true
        db.collection("Posts")
            .add(post).addOnSuccessListener {doc ->
                pushResource(doc.id).addOnSuccessListener{
                    db.collection("Posts").document(doc.id).update("id", doc.id)

                    storageRef.child("PostImages/${doc.id}").downloadUrl.addOnSuccessListener {
                        db.collection("Posts").document(doc.id).update("source", it.toString())
                        postTitle = "Titulo Publicacion"
                        _resource.value = Uri.EMPTY
                        _isSendingInfo.value = false
                        onSuccess.invoke()
                    }

                }
            }
    }

    /**simply sends the file in [_resource]
     * @param id the name the file will have (same as post)*/
    private fun pushResource(id:String): UploadTask {
        return storageRef.child("PostImages/$id").putFile(_resource.value!!)
    }

    /**setter for resource*/
    fun setResource(uri: Uri, context: Context) {
        newPost.source = uri.toString()
        val type = getMimeType(context, uri)
        if (type != null) {
            newPost.typeOfMedia = type.substring(0,5)
        }
        _resource.value = uri
    }


}