package com.example.petstagram.ViewModels

import android.annotation.SuppressLint
import android.content.Context
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Arrays
import java.util.concurrent.Semaphore

@SuppressLint("MutableCollectionMutableState", "StaticFieldLeak")
class DataFetchViewModel : ViewModel() {

    var selfId = ""

    private val _selfProfile = MutableStateFlow(Profile())

    lateinit var context: Context

    /**Firebase FireStore reference*/
    val db = Firebase.firestore

    /**actual content of [Post]s and their Uri Strings*/
    private val _posts = MutableStateFlow<List<UIPost>>(emptyList())

    /**it tells if we are loading, so if we go out and in the view again we dont
     * start another collect, it is set to true until the collection ends*/
    var alreadyLoading by mutableStateOf(false)

    /**ids of the already saved [Post]s*/
    private var ids by mutableStateOf(_posts.value.map { it.id })

    /**number of indexes we'll be getting at a time*/
    private var indexesOfPosts = 5L
    private var savedList by mutableStateOf(SavedList())

    /**gets executed on Launch, tells [_posts] to keep collecting the data from the [db]*/
    fun startLoadingPosts(context: Context){
        this.context = context

        keepUpWithUser()
        getUserPosts()
        getPostsFromFirebase()
        fetchSavedList()

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
                        }.continueWith { alreadyLoading = false }

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

        if (castedPost.likes.any { it.userId==_selfProfile.value.id })
            castedPost.liked= Pressed.True
        else
            castedPost.liked = Pressed.False

        Log.e(castedPost.liked.name, _selfProfile.value.id +" "+ castedPost.likes.map { it.userId })

        loadSaved(castedPost, postJson)

        castedPost.loadSource(context)

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
        return _posts.value.filter { it.id in savedList.postList }
    }

}