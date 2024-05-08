package com.example.petstagram.ViewModels

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.Controllers.GeneralController
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
class PostsViewModel : GeneralController(){


    /**[Category] of the posts displayed*/
    lateinit var statedCategory: Category

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
                actualUser = base.profile()

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

    override fun scroll() {
         _posts.value= base.postsFromCategory(statedCategory)
    }

}