package com.example.petstagram.ViewModels

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.Controllers.PostsUIController
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.like.Pressed
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState")
class PostsViewModel : ViewModel() ,PostsUIController{

    lateinit var base : DataFetchViewModel

    override var actualUser by mutableStateOf(Profile())

    /**Firebase FireStore reference*/
    override val db = Firebase.firestore

    /**[Category] of the posts displayed*/
    lateinit var statedCategory: Category

    /**indicates if we still dont have any [Post]s*/
    private val _isLoading = MutableLiveData(false)

    /**[LiveData] for [_isLoading]*/
    override val isLoading :LiveData<Boolean> = _isLoading

    private var commentsDisplayed by mutableStateOf(false)

    /**actual content of [Post]s and their Uri Strings*/
    private val _posts = MutableStateFlow<List<UIPost>>(emptyList())

    /**visible version of [_posts]*/
    override val posts : StateFlow<List<UIPost>> = _posts

    private val _actualComments = MutableLiveData<List<UIComment>>(emptyList())

    override val actualComments: LiveData<List<UIComment>> = _actualComments

    private var ended by mutableStateOf(false)


    /**gets executed on Launch, tells [_posts] to keep collecting the data from the [db]*/
    fun startLoadingPosts(){

        if (!_isLoading.value!!) {
            viewModelScope.launch {

                _isLoading.value = true

                val start = base.postsFromCategory(statedCategory)

                while (base.alreadyLoading){
                    delay(100)
                }

                val end = base.postsFromCategory(statedCategory)


                for (i in end){
                    if(i !in _posts.value) {
                        _posts.value += i
                        delay(500)
                    }
                }
                _isLoading.value = false

                ended = start.size >= _posts.value.size

            }

        }
    }

    fun stopLoading() {
        _posts.value = emptyList()
        base.stopLoading()
        viewModelScope.coroutineContext.cancelChildren()
    }

    override fun scroll(scrolled : Double) {
        if (scrolled>0.8){
            if(!base.alreadyLoading)
                _posts.value= base.postsFromCategory(statedCategory)
        }
    }

    override fun selectPostForComments(post: UIPost) {
        commentsDisplayed = true
        viewModelScope.launch {

            var loadingComments = true
            val result = mutableListOf<UIComment>()
            db.collection("Comments").whereEqualTo("commentPost", post.id).get()
                .addOnSuccessListener {
                    for (i in it) {
                        val comment = i.toObject(UIComment::class.java)
                        db.collection("Users").document(comment.user).get().addOnSuccessListener {userjson ->
                            comment.objectUser = userjson.toObject(Profile::class.java)!!

                            comment.liked = if(comment.likes.find { it.userId==actualUser.id }==null) Pressed.False else Pressed.True

                            result.add(comment)
                        }.continueWith {
                            loadingComments = false
                        }

                    }
                }
            while (loadingComments) delay(100)

            _actualComments.value = result

        }
    }

    override fun clearComments() {
        commentsDisplayed = false
        _actualComments.value = emptyList()
    }

}