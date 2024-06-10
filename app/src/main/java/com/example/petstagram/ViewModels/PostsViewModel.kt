package com.example.petstagram.ViewModels

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.example.petstagram.Controllers.GeneralController
import com.example.petstagram.UiData.Category
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState")
class PostsViewModel : GeneralController() {

    /**[Category] of the posts displayed*/
    lateinit var statedCategory: Category


    /**gets executed on Launch, tells [_posts] to keep collecting the data from the [db]*/
    fun startLoadingPosts() {

        if (!_isLoading.value!!) {
            viewModelScope.launch {

                _isLoading.value = true

                base.postsFromCategory(statedCategory)

                while (base.alreadyLoading) {
                    delay(10)
                }
                actualUser = base.profile()

                val end = base.postsFromCategory(statedCategory)

                for (post in end - _posts.value.toSet()) {
                    _posts.value += post
                    delay(250)

                }

                _isLoading.value = false


            }

        }
    }

    override fun scroll(generatedByScroll: Boolean) {
        if (!generatedByScroll && _posts.value.isEmpty() || generatedByScroll) {
            startLoadingPosts()
        }
    }

}