package com.example.petstagram.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _categories = MutableStateFlow<List<Category>>(emptyList())

    val categories: StateFlow<List<Category>> = _categories

    lateinit var selectedCategory: Category

    fun fetchCategories() {
        viewModelScope.launch {

            _categories.collect {

                val categories = mutableListOf<Category>()
                db.collection("Categories")
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful && !it.result.isEmpty) {
                            for (catJson in it.result) {
                                val castedCategory = catJson.toObject(Category::class.java)
                                categories.add(castedCategory)
                            }
                        }
                        _categories.value = categories
                    }
                delay(10000)
            }
        }
    }

}