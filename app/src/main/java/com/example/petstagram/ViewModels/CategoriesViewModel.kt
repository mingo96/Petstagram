package com.example.petstagram.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Profile
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
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

    private val _profiles = MutableLiveData<List<Profile>>()

    val profiles: LiveData<List<Profile>> = _profiles

    private val _searchText = MutableLiveData("")

    val searchText: LiveData<String> = _searchText

    var userid by mutableStateOf("")
        private set

    /**gets executed once at Launch, gets the categories from [base]*/
    fun fetchCategories() {
        viewModelScope.launch {

            val end = base.categories()
            for (i in end) {
                if (i.name !in _categories.value.map { it.name }) _categories.value += i
            }
            userid = base.profile().id

            search()
            delay(5000)
            search()
        }
    }

    /**stops all sub-coroutines*/
    fun stopLoading() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    /**edit text, waits a second, if text is the same, search*/
    fun setSearchText(it: String) {
        viewModelScope.launch {
            _searchText.value = it
            delay(1000)
            if (_searchText.value == it) {
                search()
                stopLoading()
            }
        }
    }

    fun search() {
        _profiles.value = base.search(searchText.value!!).sortedBy { it.userName }

    }

    fun follow(it: Profile) {

        if (!it.followers.contains(userid)) {

            base.follow(it)
            it.followers += userid
        } else {
            base.unfollow(it.id)
            it.followers -= userid
        }
    }

}