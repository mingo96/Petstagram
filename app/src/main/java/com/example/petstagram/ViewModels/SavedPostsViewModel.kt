package com.example.petstagram.ViewModels

import androidx.lifecycle.viewModelScope
import com.example.petstagram.Controllers.GeneralController
import com.example.petstagram.UiData.Category
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
                    delay(10)
                }

                actualUser = base.profile()

                val end = base.postsFromSaved()

                for (post in end- _posts.value.toSet()){
                    if(statedCategory == null || post.category!!.name == statedCategory!!.name) {
                        _posts.value += post
                        delay(500)
                    }
                }

                if(statedCategory == null)_categories.value = _posts.value.distinctBy { it.category!!.name }.map { it.category!! }

                _isLoading.value = false

            }

        }
    }

    fun selectCategory(category: Category?){
        statedCategory = if (category == statedCategory)
            null
        else category
        _posts.value = _posts.value.filter { statedCategory == null ||it.category!!.name == statedCategory!!.name }

    }
    override fun scroll() {
        startLoadingPosts()
    }
}