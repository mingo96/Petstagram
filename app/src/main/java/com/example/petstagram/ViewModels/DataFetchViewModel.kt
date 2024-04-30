package com.example.petstagram.ViewModels

import android.annotation.SuppressLint
import android.util.Log
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
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.UiData.UISavedList
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Arrays
import java.util.concurrent.Semaphore

@SuppressLint("MutableCollectionMutableState")
class DataFetchViewModel : ViewModel() {

    var selfId = ""

    private var _selfProfile = MutableStateFlow(Profile())

    /**Firebase FireStore reference*/
    val db = Firebase.firestore

    /**[Category] of the posts displayed*/
    lateinit var statedCategory: Category

    /**actual content of [Post]s and their Uri Strings*/
    private val _posts = MutableStateFlow<List<UIPost>>(emptyList())

    /**it tells if we are loading, so if we go out and in the view again we dont
     * start another collect, it is set to true until the collection ends*/
    var alreadyLoading by mutableStateOf(false)

    /**ids of the already saved [Post]s*/
    private var ids by mutableStateOf(_posts.value.map { it.id })

    /**number of indexes we'll be getting at a time*/
    private var indexesOfPosts = 5L

    private var savedList : UISavedList = UISavedList()

    var semaforo : Semaphore = Semaphore(1)

    /**gets executed on Launch, tells [_posts] to keep collecting the data from the [db]*/
    fun startLoadingPosts(){


        keepUpWithUser()
        getUserPosts()
        getPostsFromFirebase()
        fetchSavedList()

    }

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

                    if(_selfProfile.value.id!= "") {
                        delay(100)
                        db.collection("Users").whereEqualTo("id", _selfProfile.value.id).get()
                            .addOnSuccessListener {

                                val newVal = it.documents[0].toObject(Profile::class.java)!!
                                if (newVal != _selfProfile.value) {
                                    _selfProfile.value = newVal
                                }
                            }
                    }
                    delay(15000)
                }
        }
    }

    private fun fetchSavedList(){

        viewModelScope.launch {

            while(alreadyLoading) {
                delay(100)
            }
            alreadyLoading = true
            delay(100)

            db.collection("SavedLists").whereEqualTo("userId", _selfProfile.value.id).get().addOnSuccessListener { document ->
                if(!document.isEmpty) {
                    val document = document.documents.first()
                    val spareSavedList = document.toObject(UISavedList::class.java)!!

                    for (post in spareSavedList.postList) {
                        _posts.value.find { it.id == post }
                            ?.let { it1 -> spareSavedList.UIPosts.add(it1) }
                    }
                }
            }.continueWith {

                alreadyLoading = false


            }
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
                                "la carga de categoria fue completada pero no exitosa, ids = ${_posts.value.map { it.id }.toList()}"
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

        if (castedPost.likes.count { it.userId==selfId }>0)
            castedPost.liked= Pressed.True
        else
            castedPost.liked = Pressed.False

        loadSaved(castedPost, postJson)

        castedPost.loadSource()

        ids += castedPost.id

        indexesOfPosts++

    }

    private fun loadSaved(castedPost: UIPost, postJson: DocumentSnapshot){

        db.collection("SavedLists").whereEqualTo("userId", _selfProfile.value.id).whereArrayContains("postList", castedPost.id).get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    castedPost.saved= SavePressed.Si
                }
                _posts.value+=castedPost
                _posts.value = _posts.value.sortedBy { it.postedDate }.reversed()
                ids+=postJson.id
            }
    }


    fun stopLoading() {
        viewModelScope.coroutineContext.cancelChildren()
        alreadyLoading = false
    }


    fun postsFromCategory(category : Category): List<UIPost> {

        moreCategoryPosts(category)
        return _posts.value.filter { it.category!= null && it.category!!.name == category.name }
    }


    fun postsFromUser(user : String): List<UIPost> {
        return _posts.value.filter { it.creatorUser!= null && it.creatorUser!!.id == user }
    }


    fun postsFromSaved(): List<UIPost> {
        fetchSavedList()
        return savedList.UIPosts
    }

}