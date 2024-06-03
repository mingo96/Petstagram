package com.example.petstagram.ViewModels

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Notification
import com.example.petstagram.UiData.NotificationChannel
import com.example.petstagram.UiData.Pet
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.Report
import com.example.petstagram.UiData.SavedList
import com.example.petstagram.UiData.TypeOfNotification
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.UiData.UISavedList
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed
import com.example.petstagram.notifications.PetstagramNotificationService
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
import java.util.Date
import kotlin.random.Random

@SuppressLint("MutableCollectionMutableState", "StaticFieldLeak")
/**[ViewModel] that contains almost all the info, others will use it as main data source*/
class DataFetchViewModel : ViewModel() {

    /**id of the user*/
    var selfId = ""

    /**actual profile*/
    private var _selfProfile = Profile()

    private var _notificationsChannel: NotificationChannel? = null

    /**context gotten to generate the [Uri]'s for local storage*/
    lateinit var context: Context

    /**storage reference*/
    private val storageRef = Firebase.storage.reference

    /**Firebase FireStore reference*/
    val db = Firebase.firestore

    /**actual content of [Post]s*/
    private var _posts = mutableListOf<UIPost>()

    /**actual content of [Pet]s*/
    private var _pets = mutableListOf<Pet>()

    /**actual content of [Category]s*/
    private var _categories = mutableListOf<Category>()

    /**actual content of [Profile]s*/
    private var _profiles = mutableListOf<Profile>()

    /**it tells if we are loading anything yet*/
    var alreadyLoading by mutableStateOf(false)

    /**ids of the already saved [Post]s*/
    private var ids by mutableStateOf(_posts.map { it.id })

    /**number of indexes we'll be getting at a time*/
    private var indexesOfPosts = 5L

    /**actual [SavedList]s*/
    private var savedList by mutableStateOf(SavedList())

    private var snapshots: MutableList<ListenerRegistration> = mutableListOf()

    private lateinit var notificationService: PetstagramNotificationService

    /**gets executed on Launch, makes initial load*/
    fun startLoadingData(context: Context) {
        this.context = context

        notificationService = PetstagramNotificationService(context)

        fetchPetsFromUser()
        keepUpWithUser()
        getPostsFromFirebase()
        fetchSavedList()
        fetchCategories()

    }

    /**gets more [Category]*/
    private fun fetchCategories() {
        alreadyLoading = true
        db.collection("Categories").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (categoryJson in it.documents) {
                    val newCategory = categoryJson.toObject(Category::class.java)!!
                    if (newCategory.name !in _categories.map { it.name }) {
                        _categories.add(newCategory)
                    }
                }
            }
            _categories.sortBy { Random.nextInt() }
            alreadyLoading = false
        }

    }

    /**returns [Category] and tries to get more*/
    fun categories(): List<Category> {
        fetchCategories()

        return _categories.toList()
    }

    /**returns value of [_selfProfile]*/
    fun profile() = _selfProfile

    /**states a flow that keeps [_selfProfile] updated*/
    private fun keepUpWithUser() {
        viewModelScope.launch {

            //delay until we have an id
            while (selfId.isBlank()) {
                delay(100)
            }

            alreadyLoading = true
            snapshots += db.collection("Users").whereEqualTo("authId", selfId)
                .addSnapshotListener { value, error ->
                    if (value != null) {
                        val newVal = value.documents[0].toObject(Profile::class.java)!!
                        if (newVal != _selfProfile) {
                            _selfProfile = newVal
                            if (_selfProfile.id != "") {
                                updateProfileToPosts(_selfProfile)
                                prepareNotifications()
                            }
                        }

                        if (newVal !in _profiles) _profiles.add(newVal)
                        updateReportScore()
                        keepUpWithPosts()
                        alreadyLoading = false
                    }
                }

        }
    }

    private fun updateProfileToPosts(profile: Profile) {
        db.collection("Posts").whereEqualTo("creatorUser.id", profile.id).get()
            .addOnSuccessListener {
                for (i in it.documents) {
                    db.collection("Posts").document(i.id).update("creatorUser", profile)
                }
            }
    }

    private fun prepareNotifications() {
        if (_notificationsChannel == null) db.collection("NotificationsChannels")
            .whereEqualTo("user", _selfProfile.id).get().addOnSuccessListener { docs ->
                _notificationsChannel = docs.documents[0].toObject(
                    NotificationChannel::class.java
                )!!
                snapshots += db.collection("NotificationsChannels")
                    .document(_notificationsChannel!!.id).addSnapshotListener { value, error ->
                        if (value != null) {
                            val newNotif = value.toObject(
                                NotificationChannel::class.java
                            )!!

                            setLikesNotifications(newNotif)

                            val newValues =
                                newNotif.notifications - _notificationsChannel!!.notifications.toSet()
                                    .filter { !it.seen }.toSet()

                            _notificationsChannel = newNotif
                            for (i in newValues) {
                                when (i.type) {
                                    TypeOfNotification.Comment -> {
                                        newCommentNotification(i)
                                    }

                                    TypeOfNotification.Follow -> {
                                        getUser(i.sender){

                                            val user = _profiles.find { it.id == i.sender }
                                            notificationService.followingNotification(
                                                "Nuevo seguidor!",
                                                content = "${i.userName} te ha seguido!",
                                                url = user!!.profilePic
                                                )
                                        }

                                    }

                                    TypeOfNotification.NewPost -> {

                                        newPostNotification(i)
                                    }

                                    else -> {}
                                }
                                db.collection("NotificationsChannel").document(
                                    _notificationsChannel!!.id
                                ).update(
                                    "notifications.seen", true
                                )
                            }
                        }
                    }

            }
    }

    private fun newCommentNotification(i : Notification){
        viewModelScope.launch {

            val found = _posts.find { it.id == i.notificationText.split("=")[0] }
            if (found != null) {
                while (found.UIURL == Uri.EMPTY){
                    delay(100)
                }
                val bitmap: Bitmap =
                    if (found.typeOfMedia == "image")
                        uriToBitmap(context, found.UIURL)
                    else {

                        val retriever = MediaMetadataRetriever()
                        retriever.setDataSource(context, found.UIURL)
                        retriever.getFrameAtIndex(1)!!
                    }


                notificationService.showNotSoBasicNotification(
                    "Nuevo comentario de ${i.userName}!",
                    content = "${i.userName}: ${i.notificationText.split("=")[1]}",
                    bitmap
                )
            } else {

                individualPostFetch(i.notificationText.split("=")[0]) { uri, isVideo ->
                    notificationService.showNotSoBasicNotification(
                        "Nueva publicacion de ${i.userName}!",
                        content = "${i.userName}: ${i.notificationText.split("=")[1]}",
                        if (!isVideo) {
                            uriToBitmap(context, uri)
                        }else{

                            val retriever = MediaMetadataRetriever()
                            retriever.setDataSource(context, uri)
                            retriever.getFrameAtIndex(1)!!
                        }
                    )
                }
            }

        }
    }

    private fun newPostNotification(i : Notification){
        viewModelScope.launch {

            val found = _posts.find { it.id == i.notificationText.split("=")[0] }
            if (found != null) {
                while (found.UIURL == Uri.EMPTY){
                    delay(100)
                }
                val bitmap: Bitmap =
                    if (found.typeOfMedia == "image")
                        uriToBitmap(context, found.UIURL)
                    else {

                        val retriever = MediaMetadataRetriever()
                        retriever.setDataSource(context, found.UIURL)
                        retriever.getFrameAtIndex(1)!!
                    }


                notificationService.showNotSoBasicNotification(
                    "Nueva publicacion de ${i.userName}!",
                    content = "${i.userName}: ${i.notificationText.split("=")[1]}",
                    bitmap
                )
            } else {

                individualPostFetch(i.notificationText.split("=")[0]) { uri, isVideo ->
                    notificationService.showNotSoBasicNotification(
                        "Nueva publicacion de ${i.userName}!",
                        content = "${i.userName}: ${i.notificationText.split("=")[1]}",
                        if (!isVideo) {
                            uriToBitmap(context, uri)
                        }else{

                            val retriever = MediaMetadataRetriever()
                            retriever.setDataSource(context, uri)
                            retriever.getFrameAtIndex(1)!!
                        }
                    )
                }
            }

        }
    }

    private fun setLikesNotifications(newNotif: NotificationChannel) {

        val count =
            (newNotif.notifications - _notificationsChannel!!.notifications.toSet()).filter { it.type == TypeOfNotification.Like }
        val postsNotified = count.map { it.notificationText }

        for (i in postsNotified) {
            val number = count.count { it.notificationText == i }
            viewModelScope.launch {

                val found = _posts.find { it.id == i }
                if (found != null) {
                    while (found.UIURL == Uri.EMPTY){
                        delay(100)
                    }
                    val bitmap: Bitmap =
                        if (found.typeOfMedia == "image")
                            uriToBitmap(context, found.UIURL)
                        else {

                            val retriever = MediaMetadataRetriever()
                            retriever.setDataSource(context, found.UIURL)
                            retriever.getFrameAtIndex(1)!!
                        }


                    notificationService.showNotSoBasicNotification(
                        title = "A la gente le gusta tu post!",
                        content = "Tienes $number like${if (number > 1) "s" else ""} nuevo${if (number > 1) "s" else ""}!",
                        bitmap
                    )
                } else {

                    individualPostFetch(i) { uri, isVideo ->
                        notificationService.showNotSoBasicNotification(
                            title = "A la gente le gusta tu post!",
                            content = "Tienes $number like${if (number > 1) "s" else ""} nuevo${if (number > 1) "s" else ""}!",
                            if (!isVideo) {
                                uriToBitmap(context, uri)
                            }else{

                                val retriever = MediaMetadataRetriever()
                                retriever.setDataSource(context, uri)
                                retriever.getFrameAtIndex(1)!!
                            }
                        )
                    }
                }

            }
        }
    }

    fun uriToBitmap(context: Context, uri: Uri): Bitmap {

        val contentResolver: ContentResolver = context.contentResolver

        val source = ImageDecoder.createSource(contentResolver, uri)

        return ImageDecoder.decodeBitmap(source)
    }

    private fun keepUpWithPosts() {
        if (alreadyLoading)

            snapshots += db.collection("Posts").whereEqualTo("creatorUser.id", _selfProfile.id)
                .addSnapshotListener { value, error ->
                    if (value != null) {
                        for (i in value.documents) {
                            if (i.id !in _posts.map { it.id }) {
                                bootUpPost(i)
                            }
                        }
                    }
                }
    }

    private fun getUser(user: String, afterFetch: (String) -> Unit = {}) {
        if (user !in _profiles.map { it.id }) {
            db.collection("Users").document(user).get().addOnSuccessListener {
                val newVal = it.toObject(Profile::class.java)!!
                _profiles.add(newVal)
                afterFetch(newVal.userName)
            }
        }else{
            afterFetch("")
        }
    }

    /**gets our actual report score for [_selfProfile]*/
    private fun updateReportScore() {

        db.collection("Reports").whereEqualTo("user", _selfProfile.id).get().addOnSuccessListener {

            val before = _selfProfile.reportScore
            for (i in it.documents) {
                val report = i.toObject(Report::class.java)
                if (report!!.reportDate.daysAgo() >= 10 && _selfProfile.reportScore >= 0.1) {
                    _selfProfile.reportScore -= 0.1
                }
            }
            if (before != _selfProfile.reportScore) db.collection("Users").document(_selfProfile.id)
                .update("reportScore", _selfProfile.reportScore)

        }
    }

    /**how many days have passed from the date that calls it*/
    private fun Date.daysAgo(): Long {
        return this.time - Date.from(Instant.now()).time / 1000 / 60 / 60 / 24
    }

    /**updates [savedList]*/
    private fun fetchSavedList() {

        viewModelScope.launch {

            while (alreadyLoading) {
                delay(100)
            }
            alreadyLoading = true
            delay(100)

            db.collection("SavedLists").whereEqualTo("userId", _selfProfile.id).get()
                .addOnSuccessListener { document ->
                    if (!document.isEmpty) {
                        val document = document.documents.first()
                        val spareSavedList = document.toObject(UISavedList::class.java)!!
                        savedList = spareSavedList

                        for (id in spareSavedList.postList) {
                            individualPostFetch(id)
                        }
                    }
                }.continueWith {

                    alreadyLoading = false

                }
        }
    }

    /**gets one post given its id*/
    private fun individualPostFetch(id: String, laterOn: (Uri, Boolean) -> Unit = {uri, isvideo->}) {
        try {

            db.collection("Posts").document(id).get().addOnSuccessListener {
                if (it.exists()) bootUpPost(it, laterOn)
            }
        } catch (e: Exception) {
            Toast.makeText(
                context, "Una publicación ha dejado de existir en tus guardados", Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**gets [Post] JSONs, ordered by [Post.postedDate], if it fails it calls itself*/
    private fun getPostsFromFirebase() {


        viewModelScope.launch {

            while (alreadyLoading) {
                delay(100)
            }
            alreadyLoading = true
            delay(100)


            db.collection("Posts")
                //request filters
                .orderBy("postedDate", Query.Direction.DESCENDING).limit(indexesOfPosts).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (!it.result.isEmpty) {

                            //case not empty
                            for (postJson in it.result.documents) {
                                bootUpPost(postJson)

                            }
                        }

                        alreadyLoading = false
                    } else {
                        if (it.isCanceled) {
                            Log.i("Error de carga", "la carga general fue cancelada")
                            getPostsFromFirebase()

                        }
                        if (it.isComplete) {
                            Log.i(
                                "Error de carga",
                                "la carga general fue completada pero no exitosa, tenemos ${ids.size}"
                            )
                            alreadyLoading = false
                        }
                    }
                }
        }

    }

    /**gets all the posts that correspond to the given id
     * @param id the id of the user, if not given, [_selfProfile]'s, if it fails it calls itself*/
    private fun getUserPosts(id: String = _selfProfile.id) {

        viewModelScope.launch {

            while (alreadyLoading) {
                delay(100)
            }
            alreadyLoading = true
            delay(100)

            db.collection("Posts")
                //filters
                .whereEqualTo("creatorUser.id", id)
                .orderBy("postedDate", Query.Direction.DESCENDING).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (!it.result.isEmpty) {

                            //case not empty
                            for (postJson in it.result.documents) {
                                bootUpPost(postJson)
                            }
                        }

                        alreadyLoading = false
                    } else {
                        if (it.isCanceled) {
                            Log.e("Error de carga", "la carga del usuario fue cancelada")

                            getUserPosts()
                        }
                        if (it.isComplete) {
                            Log.d(
                                "Error de carga",
                                "la carga del usuario fue completada pero no exitosa, tenemos ${ids.size}"
                            )
                            alreadyLoading = false
                        }
                    }
                }

        }
    }

    /**gets more [Post] from the given [Category], if it fails it calls itself*/
    private fun moreCategoryPosts(category: Category) {
        viewModelScope.launch {

            while (alreadyLoading) {
                delay(100)
            }

            alreadyLoading = true
            delay(100)

            db.collection("Posts")
                //filters
                .whereEqualTo("category", category)
                .orderBy("postedDate", Query.Direction.DESCENDING).limit(indexesOfPosts).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (!it.result.isEmpty) {

                            //case not empty
                            for (postJson in it.result.documents) {
                                bootUpPost(postJson)

                            }
                        }

                        alreadyLoading = false
                    } else {
                        if (it.isCanceled) {
                            Log.e("Error de carga", "la carga de categoria fue cancelada")

                            moreCategoryPosts(category)
                        }
                        if (it.isComplete) {
                            Log.d(
                                "Error de carga",
                                "la carga de categoria fue completada pero no exitosa, ids = ${
                                    _posts.map { it.id }.toList()
                                }"
                            )

                            alreadyLoading = false
                        }
                    }
                }

        }
    }

    /**gets all posts that correspond to a [Pet], if it fails it calls itself
     * @param id the [Pet]'s id*/
    private fun fetchPostsFromPet(id: String) {

        viewModelScope.launch {

            while (alreadyLoading) {
                delay(100)
            }
            alreadyLoading = true
            delay(100)

            db.collection("Posts")
                //filters
                .whereEqualTo("pet", id).orderBy("postedDate", Query.Direction.DESCENDING).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (!it.result.isEmpty) {

                            //case not empty
                            for (postJson in it.result.documents) {
                                bootUpPost(postJson)
                            }
                        }

                        alreadyLoading = false
                    } else {
                        if (it.isCanceled) {
                            Log.e("Error de carga", "la carga de la mascota fue cancelada")

                            getUserPosts()
                        }
                        if (it.isComplete) {
                            Log.d(
                                "Error de carga",
                                "la carga dela mascota fue completada pero no exitosa, tenemos ${ids.size}"
                            )
                            alreadyLoading = false
                        }
                    }
                }

        }
    }

    /**given a JSON, parses it to a [Post] and prepares it to be displayed*/
    private fun bootUpPost(postJson: DocumentSnapshot, laterOn: (Uri, Boolean) -> Unit = {uri, isVideo->}) {

        val castedPost = postJson.toObject(UIPost::class.java)!!

        if (castedPost.id in ids || castedPost.id.isBlank() || castedPost.category == null) return;

        loadSaved(castedPost)

        if (castedPost.likes.any { it.userId == _selfProfile.id }) castedPost.liked = Pressed.True

        if (castedPost.pet.isNotBlank()) {
            loadPet(castedPost)
        }

        if (castedPost.creatorUser !in _profiles) _profiles.add(castedPost.creatorUser!!)

        //download to a temporary file
        try {

            if (!File(
                    castedPost.typeOfMedia + "s",
                    if (castedPost.typeOfMedia == "video") "mp4" else "jpeg"
                ).exists()
            ) {

                val destination = File.createTempFile(
                    castedPost.typeOfMedia + "s",
                    if (castedPost.typeOfMedia == "video") "mp4" else "jpeg"
                )

                storageRef.child("PostImages/${castedPost.id}").getFile(destination)
                    .addOnSuccessListener {
                        castedPost.UIURL = Uri.fromFile(destination)

                        if (castedPost.typeOfMedia == "video") castedPost.mediaItem =
                            MediaItem.fromUri(castedPost.UIURL)
                    }
            } else {
                castedPost.UIURL = Uri.fromFile(
                    File(
                        castedPost.typeOfMedia + "s",
                        if (castedPost.typeOfMedia == "video") "mp4" else "jpeg"
                    )
                )
            }
            laterOn(castedPost.UIURL, castedPost.typeOfMedia == "video")
        } catch (e: Exception) {
            Log.e("tipo", e.stackTraceToString())
            //source doesnt exist, erase it
            _posts.removeIf { it.id == castedPost.id }
        }

        ids += castedPost.id

        indexesOfPosts++

    }


    /**gets the [Pet] from the given [Post] as a whole item*/
    private fun loadPet(castedPost: UIPost) {

        if (_pets.any { it.id == castedPost.pet }) {
            castedPost.uiPet = _pets.find { it.id == castedPost.pet }
        } else {
            db.collection("Pets").document(castedPost.pet).get().addOnSuccessListener { doc ->
                val addedPet = doc.toObject(Pet::class.java)!!
                castedPost.uiPet = addedPet
                if (doc.id !in _pets.map { it.id }) {
                    _pets.add(addedPet)
                }
            }
        }

    }

    /**sets [UIPost.saved] up for the ui*/
    private fun loadSaved(castedPost: UIPost) {

        db.collection("SavedLists").whereEqualTo("userId", _selfProfile.id)
            .whereArrayContains("postList", castedPost.id).get().addOnSuccessListener {
                if (!it.isEmpty) {
                    castedPost.saved = SavePressed.Si
                }
                _posts += castedPost
                _posts = _posts.sortedBy { it.postedDate }.reversed().toMutableList()
                ids += castedPost.id
            }
    }

    /**gets all pets from given user
     * @param id id of the user, if not given its [_selfProfile]'s*/
    private fun fetchPetsFromUser(id: String = _selfProfile.id) {
        db.collection("Pets").whereEqualTo("owner", id).get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (petJson in it.documents) {
                    if (petJson.id !in _pets.map { it.id }) {
                        val newPet = petJson.toObject(Pet::class.java)!!
                        _pets.add(newPet)
                    }
                }
            }
        }
    }

    /**stops all current processes*/
    fun stopLoading() {
        viewModelScope.coroutineContext.cancelChildren()
        alreadyLoading = false
    }

    /**asks for more posts of the given category while returns current ones*/
    fun postsFromCategory(category: Category): List<UIPost> {

        moreCategoryPosts(category)
        return _posts.filter { it.category != null && it.category!!.name == category.name }
    }

    /**asks for more posts of the given user while returns current ones*/
    fun postsFromUser(user: String): List<UIPost> {

        getUserPosts(user)
        return _posts.filter { it.creatorUser != null && it.creatorUser!!.id == user }
    }

    /**asks for more posts of the given Saved list while returns current ones*/
    fun postsFromSaved(): List<UIPost> {
        fetchSavedList()
        return _posts.filter { it.id in savedList.postList }
    }

    /**asks for pets of the given user while returns current ones*/
    fun petsFromUser(id: String): List<Pet> {
        fetchPetsFromUser(id)
        return _pets.filter { it.owner == id }
    }

    /**asks for more posts of the pet category while returns current ones*/
    fun postsFromPet(pet: Pet): List<UIPost> {
        fetchPostsFromPet(pet.id)

        return _posts.filter { it.pet == pet.id }
    }

    /**clears [_posts] list*/
    fun clear() {
        _posts = mutableListOf()
        snapshots.forEach {
            it.remove()
            notificationService.cancelNotification(snapshots.indexOf(it))
        }
        _notificationsChannel = null

        ids = emptyList()
    }
}