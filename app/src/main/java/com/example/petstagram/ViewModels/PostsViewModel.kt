package com.example.petstagram.ViewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState")
class PostsViewModel : ViewModel() {

    /**Firebase FireStore reference*/
    private val db = Firebase.firestore

    /**[Category] of the posts displayed*/
    lateinit var statedCategory: Category

    /**indicates if we still dont have any [Post]s*/
    private val _isloading = MutableLiveData(true)

    /**[LiveData] for [_isloading]*/
    val isLoading :LiveData<Boolean> = _isloading

    /*******TO BE TESTED******
     * it's supposed to locally save content for categories when we swap between them*/
    private val locallySaved by mutableStateOf( mutableMapOf<Category, List<Post>>() )

    /**actual content of [Post]s and their Uri Strings*/
    private val _posts = MutableStateFlow<List<Post>>(emptyList())

    /**it tells if we are loading, so if we go out and in the view again we dont
     * start another collect, it is set to true until the collection ends*/
    private var alreadyLoading by mutableStateOf(false)

    /**visible version of [_posts]*/
    val posts : StateFlow<List<Post>> = _posts

    /**ids of the already saved [Post]s*/
    private var ids by mutableStateOf(_posts.value.map { it.id })

    /**number of indexes we'll be getting at a time*/
    private var indexesOfPosts = 10L

    /**gets executed on Launch, tells [_posts] to keep collecting the data from the [db]*/
    fun startLoadingPosts(){

        if (!alreadyLoading) {
            alreadyLoading = true
            if (locallySaved.map { it.key.name }.contains(statedCategory.name)) {
                //if i dont do this it fails because it has to be the SAME category, same object reference
                val localCategory = locallySaved.keys.find { it.name == statedCategory.name }!!

                _posts.value = locallySaved[localCategory]!!
                indexesOfPosts = _posts.value.count().toLong()+10
            } else {


                indexesOfPosts = 10L
                _posts.value = emptyList()
                locallySaved[statedCategory] = emptyList()
            }
            viewModelScope.launch {
                Log.i("sadiasgf", "se empieza")

                delay(1000)
                getPostsFromFirebase()
                delay(1000)
                //if we dont have any post yet, we are loading
                _isloading.value = (_posts.value.isEmpty())
                if (_posts.value.count().toLong() >= indexesOfPosts)
                    indexesOfPosts += 10

            }


        }
    }

    /**gets [Post] JSONs, filtering them for the [statedCategory] we have, ordered by [Post.postedDate]*/
    fun getPostsFromFirebase(){
        db.collection("Posts")
            //request filters
            .orderBy("postedDate", Query.Direction.DESCENDING)
            .whereEqualTo("category", statedCategory)
            //max amount we're getting is indexesOfPosts, TODO make it increment when needed
            .limit(indexesOfPosts)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty){
                    //if there's any entry
                    for (postJson in querySnapshot.documents){
                        //only get the data loaded if we dont have it registered
                        if(postJson.id !in ids) {
                            savePostAndUrl(postJson)
                        }
                    }
                }
            }.continueWith {
                alreadyLoading = false
            }
    }

    /**given a JSON, saves its [Post] info and its url*/
    private fun savePostAndUrl(postJson: DocumentSnapshot) {

        val castedPost = postJson.toObject(Post::class.java)

        _posts.value+=castedPost!!
        _posts.value = _posts.value.sortedBy { it.postedDate }.reversed()
        ids+=postJson.id
        locallySaved[statedCategory] = _posts.value


    }

    fun stopLoading() {
        Log.i("getting", "out")
        for (i in locallySaved)
        {
            Log.i(i.key.name, i.value.size.toString())
        }
        Log.i("teniamos", _posts.value.size.toString())
        Log.i("ids", ids.size.toString())
        viewModelScope.coroutineContext.cancelChildren()
    }

    fun scroll(scrolled : Double) {
        if (scrolled>0.8){
            startLoadingPosts()
        }
    }


}