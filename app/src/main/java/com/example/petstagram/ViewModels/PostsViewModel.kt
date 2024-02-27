package com.example.petstagram.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Post
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostsViewModel : ViewModel() {

    private val db = Firebase.firestore

    lateinit var statedCategory: Category

    private val _posts = MutableStateFlow<List<Post>>(emptyList())

    val posts : StateFlow<List<Post>> = _posts


    fun fetchPosts(){
        viewModelScope.launch {

            _posts.collect{

                val posts = mutableListOf<Post>()
                db.collection("Posts")
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful && !it.result.isEmpty){
                            for (catJson in it.result){
                                val castedPost = catJson.toObject(Post::class.java)
                                posts.add(castedPost)
                            }
                        }
                        _posts.value=posts
                    }
                delay(10000)
            }
        }
    }


}