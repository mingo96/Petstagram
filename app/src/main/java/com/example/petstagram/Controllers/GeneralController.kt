package com.example.petstagram.Controllers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.ViewModels.DataFetchViewModel
import com.example.petstagram.like.Pressed
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class GeneralController : ViewModel(), PostsUIController {

    private val _optionsClicked: MutableLiveData<UIPost?> = MutableLiveData(null)

    override val optionsClicked: LiveData<UIPost?> = _optionsClicked

    lateinit var base: DataFetchViewModel

    override var storageRef = Firebase.storage.reference

    /**Firebase FireStore reference*/
    override val db = Firebase.firestore

    override var actualUser by mutableStateOf(Profile())

    private val _funnyAhhString = MutableStateFlow("")

    override val funnyAhhString: StateFlow<String> = _funnyAhhString

    /**indicates if we still dont have any [Post]s*/
    protected val _isLoading = MutableLiveData(false)

    /**[LiveData] for [_isLoading]*/
    override val isLoading: LiveData<Boolean> = _isLoading

    /**actual content of [Post]s and their Uri Strings*/
    protected val _posts = MutableStateFlow<List<UIPost>>(emptyList())

    /**visible version of [_posts]*/
    override val posts: StateFlow<List<UIPost>> = _posts

    private val _actualComments = MutableLiveData<List<UIComment>>(emptyList())

    override val actualComments: LiveData<List<UIComment>> = _actualComments

    private val _commenting = MutableLiveData(false)

    override var commenting: LiveData<Boolean> = _commenting

    private val _commentContent = MutableLiveData("")

    override var commentContent: LiveData<String> = _commentContent

    override lateinit var navController: NavHostController

    private val _videoStopped = MutableLiveData(true)

    override val videoStopped: LiveData<Boolean> = _videoStopped

    override var erasing: Boolean = false

    private val _videoMode = MutableLiveData<Boolean>(false)

    override val videoMode: LiveData<Boolean> = _videoMode

    private val _videoIsRunning: MutableLiveData<Boolean?> = MutableLiveData(false)

    override val videoIsRunning: LiveData<Boolean?> = _videoIsRunning

    private val _likedPost = MutableLiveData<UIPost?>(null)

    override val likedPost: LiveData<UIPost?> = _likedPost

    override fun animateVideoMode() {
        if (_videoMode.value == false)

            viewModelScope.launch {

                _videoMode.value = true
                delay(1000)
                _videoMode.value = false

            }
    }

    override fun animatePause() {
        if (_videoIsRunning.value != null) {
            viewModelScope.launch {
                val spare = _videoIsRunning.value!!
                _videoIsRunning.value = null
                delay(1000)
                _videoIsRunning.value = !spare
            }
        }
    }

    override fun animateLike(post: UIPost) {

        if (_likedPost.value == null)
            viewModelScope.launch {
                _likedPost.value = post
                delay(500)
                _likedPost.value = null
            }

    }

    override fun startRollingDots() {
        viewModelScope.launch {

            _funnyAhhString
                //we make it so it doesnt load more if we get out of the app
                .stateIn(
                    viewModelScope,
                    started = SharingStarted.WhileSubscribed(10000),
                    0
                )
                .collect {
                    _funnyAhhString.value = when (_funnyAhhString.value) {
                        "." -> ".."
                        ".." -> "..."
                        "..." -> ""
                        else -> "."
                    }
                    delay(500)

                }
        }
    }

    override fun selectPostForComments(post: UIPost) {
        viewModelScope.launch {

            var loadingComments = true
            val result = mutableListOf<UIComment>()
            db.collection("Comments").whereEqualTo("commentPost", post.id).get()
                .addOnSuccessListener {
                    for (i in it) {
                        val comment = i.toObject(UIComment::class.java)
                        db.collection("Users").document(comment.user).get()
                            .addOnSuccessListener { userjson ->
                                comment.objectUser = userjson.toObject(Profile::class.java)!!

                                comment.liked =
                                    if (comment.likes.find { it.userId == actualUser.id } == null) Pressed.False else Pressed.True

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

    override fun deletePost(post: UIPost) {

        db.collection("Posts").document(post.id).delete()
        storageRef.child("PostImages").child(post.id).delete()
        _posts.value-=post
    }

    override fun commentingToggle() {
        _commenting.value = !_commenting.value!!
    }

    override fun textChange(newText: String) {
        _commentContent.value = newText
    }

    override fun clearComments() {
        _commenting.value = false
        _actualComments.value = emptyList()
    }

    fun stopLoading() {
        base.stopLoading()
        _posts.value = emptyList()
        _isLoading.value = false
        viewModelScope.coroutineContext.cancelChildren()
    }

    override fun optionsClicked(post: UIPost) {
        _optionsClicked.value = if (post != _optionsClicked.value)
            post
        else
            null
    }

    override fun clearOptions() {
        _optionsClicked.value = null
    }

    override fun toggleStop() {
        _videoStopped.value = !_videoStopped.value!!
    }
}