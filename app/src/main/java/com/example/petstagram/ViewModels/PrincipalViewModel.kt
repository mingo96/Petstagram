package com.example.petstagram.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class PrincipalViewModel : ViewModel() {

    private val db = Firebase.firestore

    val categories :StateFlow<Category> = db.getCategories()
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), object )

    fun FirebaseFirestore.getCategories(): Flow<List<Category>> {

    }

}