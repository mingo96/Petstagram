package com.example.petstagram.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.util.rangeTo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Pet
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.Report
import com.example.petstagram.UiData.SavedList
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.UiData.UISavedList
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
import java.util.Date

@SuppressLint("MutableCollectionMutableState", "StaticFieldLeak")
class DataFetchViewModel : ViewModel() {

    var selfId = ""

    val id : String
        get() {return _selfProfile.value.id}

    private val _selfProfile = MutableStateFlow(Profile())

    lateinit var context: Context

    private val storageRef = Firebase.storage.reference

    /**Firebase FireStore reference*/
    val db = Firebase.firestore

    /**actual content of [Post]s and their Uri Strings*/
    private var _posts = mutableListOf<UIPost>()

    private var _pets = mutableListOf<Pet>()

    private var _categories = mutableListOf<Category>()

    /**it tells if we are loading, so if we go out and in the view again we dont
     * start another collect, it is set to true until the collection ends*/
    var alreadyLoading by mutableStateOf(false)

    /**ids of the already saved [Post]s*/
    private var ids by mutableStateOf(_posts.map { it.id })

    /**number of indexes we'll be getting at a time*/
    private var indexesOfPosts = 5L

    private var savedList by mutableStateOf(SavedList())

    /**gets executed on Launch, tells [_posts] to keep collecting the data from the [db]*/
    fun startLoadingPosts(context: Context){
        this.context = context

        fetchPetsFromUser()
        keepUpWithUser()
        getUserPosts()
        getPostsFromFirebase()
        fetchSavedList()
        fetchCategories()

    }

    private fun fetchCategories(){
        alreadyLoading = true
        db.collection("Categories").get().addOnSuccessListener {
            if (!it.isEmpty){
                for (categoryJson in it.documents){
                    val newCategory = categoryJson.toObject(Category::class.java)!!
                    if (newCategory.name !in _categories.map { it.name }){
                        _categories.add(newCategory)
                    }
                }
            }
            alreadyLoading = false
        }

    }

    fun categories():List<Category>{
        fetchCategories()

        return _categories.toList()
    }

    fun profile()=_selfProfile.value

    private fun keepUpWithUser(){
        viewModelScope.launch {

            _selfProfile
                //we make it so it doesnt load more if we get out of the app
                .stateIn(
                    viewModelScope,
                    started = SharingStarted.WhileSubscribed(10000),
                    0
                )
                .collect{
                    while (selfId.isBlank()){
                        delay(100)
                    }

                    alreadyLoading = true
                    db.collection("Users").whereEqualTo("authId", selfId).get()
                        .addOnSuccessListener {

                            val newVal = it.documents[0].toObject(Profile::class.java)!!
                            if (newVal != _selfProfile.value){
                                _selfProfile.value = newVal
                            }
                            updateReportScore()

                        }.continueWith { alreadyLoading = false }

                    delay(15000)
                }
        }
    }

    fun updateReportScore(){

        db.collection("Reports").whereEqualTo("user", _selfProfile.value.id).get().addOnSuccessListener {

            val before = _selfProfile.value.reportScore
            for (i in it.documents){
                val report = i.toObject(Report::class.java)
                if (report!!.reportDate.daysAwayFrom()>=10 && _selfProfile.value.reportScore>=0.1){
                    _selfProfile.value.reportScore-=0.1
                }
            }
            if (before != _selfProfile.value.reportScore)
                db.collection("Users").document(_selfProfile.value.id).update("reportScore", _selfProfile.value.reportScore)

        }
    }

    fun Date.daysAwayFrom(other : Date = Date.from(Instant.now())): Long {
        return this.time - Date.from(Instant.now()).time/1000/60/60/24
    }

    private fun fetchSavedList(){

        viewModelScope.launch {

            while(alreadyLoading) {
                delay(100)
            }
            alreadyLoading = true
            db.collection("SavedLists").whereEqualTo("userId", _selfProfile.value.id).get().addOnSuccessListener { document ->
                if(!document.isEmpty) {
                    val document = document.documents.first()
                    val spareSavedList = document.toObject(UISavedList::class.java)!!
                    savedList = spareSavedList

                    for (id in spareSavedList.postList){
                        individualPostFetch(id)
                    }
                }
            }.continueWith {

                alreadyLoading = false

            }
        }
    }

    private fun individualPostFetch(id :String){
        db.collection("Posts").document(id).get().addOnSuccessListener {
            bootUpPost(it)
        }
    }

    /**gets [Post] JSONs, filtering them for the [statedCategory] we have, ordered by [Post.postedDate]*/
    private fun getPostsFromFirebase(){


        viewModelScope.launch {

            while(alreadyLoading) {
                delay(100)
            }
            alreadyLoading = true
            delay(100)


            db.collection("Posts")
                //request filters
                .orderBy("postedDate", Query.Direction.DESCENDING)
                .limit(indexesOfPosts)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        if (!it.result.isEmpty) {

                            //case not empty
                            for (postJson in it.result.documents) {
                                bootUpPost(postJson)

                            }
                        }

                        alreadyLoading = false
                    }else{
                        if(it.isCanceled) {
                            Log.i("Error de carga", "la carga general fue cancelada")
                            getPostsFromFirebase()

                        }
                        if(it.isComplete) {
                            Log.i("Error de carga", "la carga general fue completada pero no exitosa, tenemos ${ids.size}")
                            alreadyLoading = false
                        }
                    }
                }
        }

    }


    private fun getUserPosts(){

        viewModelScope.launch {

            while(alreadyLoading) {
                delay(100)
            }
            alreadyLoading = true
            delay(100)

            db.collection("Posts")
                //filters
                .whereEqualTo("creatorUser", _selfProfile.value)
                .orderBy("postedDate", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        if (!it.result.isEmpty) {

                            //case not empty
                            for (postJson in it.result.documents) {
                                bootUpPost(postJson)
                            }
                        }

                        alreadyLoading = false
                    }else{
                        if(it.isCanceled) {
                            Log.e("Error de carga", "la carga del usuario fue cancelada")

                            getUserPosts()
                        }
                        if(it.isComplete) {
                            Log.d("Error de carga", "la carga del usuario fue completada pero no exitosa, tenemos ${ids.size}")
                            alreadyLoading = false
                        }
                    }
                }

        }
    }


    private fun moreCategoryPosts(category: Category){
        viewModelScope.launch {

            while(alreadyLoading) {
                delay(100)
            }
            delay(100)

            alreadyLoading = true

            db.collection("Posts")
                //filters
                .whereEqualTo("category", category)
                .orderBy("postedDate", Query.Direction.DESCENDING)
                .limit(indexesOfPosts)
                .get()
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
                                "la carga de categoria fue completada pero no exitosa, ids = ${_posts.map { it.id }.toList()}"
                            )

                            alreadyLoading = false
                        }
                    }
                }

        }
    }

    /**given a JSON, saves its [Post] info and its url*/
    private fun bootUpPost(postJson: DocumentSnapshot) {

        if (postJson.id in ids) return;
        val castedPost = postJson.toObject(UIPost::class.java)!!

        if (castedPost.likes.any { it.userId==_selfProfile.value.id })
            castedPost.liked= Pressed.True
        else
            castedPost.liked = Pressed.False

        loadSaved(castedPost, postJson)

        val destination = File.createTempFile(castedPost.typeOfMedia+"s", if (castedPost.typeOfMedia == "video") "mp4" else "jpeg")

        storageRef.child("PostImages/${castedPost.id}").getFile(destination).addOnSuccessListener {
            castedPost.UIURL = Uri.fromFile(destination)

            if (castedPost.typeOfMedia == "video")
                castedPost.mediaItem = MediaItem.fromUri(castedPost.UIURL)
        }

        ids += castedPost.id

        indexesOfPosts++

    }

    private fun loadSaved(castedPost: UIPost, postJson: DocumentSnapshot){

        db.collection("SavedLists").whereEqualTo("userId", _selfProfile.value.id).whereArrayContains("postList", castedPost.id).get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    castedPost.saved= SavePressed.Si
                }
                _posts+=castedPost
                _posts = _posts.sortedBy { it.postedDate }.reversed().toMutableList()
                ids+=postJson.id
            }
    }

    private fun fetchPetsFromUser(id: String = _selfProfile.value.id) {
        db.collection("Pets").whereEqualTo("owner", id).get().addOnSuccessListener {
            if (!it.isEmpty){
                for (petJson in it.documents){
                    if (petJson.id !in _pets.map { it.id }) {
                        val newPet = petJson.toObject(Pet::class.java)!!
                        _pets.add(newPet)
                    }
                }
            }
        }
    }



    fun stopLoading() {
        viewModelScope.coroutineContext.cancelChildren()
        alreadyLoading = false
    }


    fun postsFromCategory(category : Category): List<UIPost> {

        moreCategoryPosts(category)
        return _posts.filter { it.category!= null && it.category!!.name == category.name }
    }


    fun postsFromUser(user : String): List<UIPost> {
        return _posts.filter { it.creatorUser!= null && it.creatorUser!!.id == user }
    }


    fun postsFromSaved(): List<UIPost> {
        fetchSavedList()
        return _posts.filter { it.id in savedList.postList }
    }

    fun petsFromUser(id: String): List<Pet> {
        fetchPetsFromUser(id)
        return _pets.filter { it.owner == id }
    }
}