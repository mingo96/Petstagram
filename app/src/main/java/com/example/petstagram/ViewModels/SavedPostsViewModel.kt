package com.example.petstagram.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.petstagram.Controllers.GeneralController
import com.example.petstagram.UiData.Category
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavedPostsViewModel : GeneralController() {

    var statedCategory :Category? = null

    private val _categories = MutableStateFlow<List<Category>>(emptyList())

    val categories : StateFlow<List<Category>> = _categories

    fun startLoadingPosts(){

        if (!_isLoading.value!!) {
            viewModelScope.launch {

                _isLoading.value = true

                base.postsFromSaved()

                while (base.alreadyLoading){
                    delay(100)
                }

                actualUser = base.profile()

                val end = base.postsFromSaved()


                for (post in end){
                    if(post !in _posts.value) {
                        _posts.value += post
                        delay(500)
                    }
                }
                _posts.value.map { it.category }.distinct().forEach { if (it!!.name !in _categories.value.map { it.name }) _categories.value += it }

                _posts.value = _posts.value.filter { statedCategory == null ||it.category!!.name == statedCategory!!.name }

                _isLoading.value = false

            }

        }
    }

    fun selectCategory(category: Category?){
        statedCategory = if (category == statedCategory)
            null
        else category
        startLoadingPosts()
    }
    override fun scroll() {
        startLoadingPosts()
    }
}