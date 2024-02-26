package com.example.petstagram.ViewModels

import android.os.Debug
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class PrincipalViewModel : ViewModel() {

    private val db = Firebase.firestore

    val _categories = MutableStateFlow<List<Category>>(emptyList())

    val categories: StateFlow<List<Category>> = _categories

    lateinit var selectedCategory : Category

    fun fetchCategories(){
        val categories = mutableListOf<Category>()
        db.collection("Categories")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && !it.result.isEmpty){
                    for (catJson in it.result){
                        val castedCategory = catJson.toObject(Category::class.java)
                        categories.add(castedCategory)
                    }
                }
                _categories.value=categories
            }
    }



}