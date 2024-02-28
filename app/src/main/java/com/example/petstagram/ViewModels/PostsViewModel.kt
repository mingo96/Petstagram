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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

    /**Firebase Storage reference*/
    private val storageRef = Firebase.storage.getReferenceFromUrl("gs://petstagram-2e298.appspot.com")

    /**[Category] of the posts displayed*/
    lateinit var statedCategory: Category

    /**indicates if we still dont have any [Post]s*/
    private val _isloading = MutableLiveData(true)

    /**[LiveData] for [_isloading]*/
    val isLoading :LiveData<Boolean> = _isloading

    /*******TO BE TESTED******
     * it's supposed to locally save content for categories when we swap between them*/
    private val locallySaved by mutableStateOf(mutableMapOf<Category, List<Pair<String, Post>>>())

    /**actual content of [Post]s and their Uri Strings*/
    private val _posts = MutableStateFlow<List<Pair<String, Post>>>(emptyList())

    /**visible version of [_posts]*/
    val posts : StateFlow<List<Pair<String, Post>>> = _posts

    /**ids of the already saved [Post]s*/
    private var ids by mutableStateOf(_posts.value.map { it.second.id })

    /**number of indexes we'll be getting at a time*/
    private var indexesOfPosts = 10L

    /**gets executed on Launch, tells [_posts] to keep collecting the data from the [db]*/
    fun startLoadingPosts(){
        viewModelScope.launch {

            _posts
                //we make it so it doesnt load more if we get out of the app
                .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(10000),
                0
            )
                .collect{

                //****TO BE TESTED**** supposedly changes the _posts value for the saved one for this category
                //if it has already been loaded

                if (locallySaved.contains(statedCategory)) {
                    _posts.value = locallySaved[statedCategory]!!
                    indexesOfPosts = _posts.value.count().toLong()
                } else {
                    indexesOfPosts = 10L
                    locallySaved[statedCategory] = _posts.value
                }

                delay(2000)
                getPostsFromFirebase()
                delay(4000)
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
            }
    }

    /**given a JSON, saves its [Post] info and its url*/
    private fun savePostAndUrl(postJson: DocumentSnapshot) {
        ids+=postJson.id

        val castedPost = postJson.toObject(Post::class.java)

        //obtenemos la url de la imagen, una vez hecho la añadimos a _posts
        storageRef.child("/PostImages/${postJson.id}").downloadUrl.addOnSuccessListener { uri ->
            _posts.value+=(Pair(uri.toString(), castedPost!!))
            _posts.value = _posts.value.sortedBy { it.second.postedDate }.reversed()
            locallySaved[statedCategory] = _posts.value
        }

    }


}