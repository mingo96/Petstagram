package com.example.petstagram.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    lateinit var base: DataFetchViewModel

    /**list of [Category] to be shown*/
    private val _categories = MutableStateFlow<List<Category>>(emptyList())

    /**visible version of [_categories]*/
    val categories: StateFlow<List<Category>> = _categories

    /**selected [Category]*/
    var selectedCategory: Category by mutableStateOf(Category())

    /**gets executed once at Launch, tells [_categories] to keep collecting info from [base]*/
    fun fetchCategories() {
        viewModelScope.launch {

            val end = base.categories()
            for (i in end) {
                if (i.name !in _categories.value.map { it.name }) _categories.value += i
            }

        }
    }

    /**stops loading*/
    fun stopLoading() {
        viewModelScope.coroutineContext.cancelChildren()
    }

}