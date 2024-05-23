package com.example.petstagram.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    lateinit var base: DataFetchViewModel

    /**Firebase Firestore reference*/
    private val db = Firebase.firestore

    /**list of [Category] to be shown*/
    private val _categories = MutableStateFlow<List<Category>>(emptyList())

    /**visible version of [_categories]*/
    val categories: StateFlow<List<Category>> = _categories

    /**selected [Category]*/
    var selectedCategory: Category by mutableStateOf(Category())

    /**gets executed once at Launch, tells [_categories] to keep collecting info from [base]*/
    fun fetchCategories() {
        viewModelScope.launch {

            _categories.collect {

                _categories.value = base.categories()
                delay(2000)

                _categories.value = base.categories()
            }
        }
    }

    /**stops loading*/
    fun stopLoading() {
        viewModelScope.coroutineContext.cancelChildren()
    }

}