package com.example.petstagram.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Post
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class PostsViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val storageRef = Firebase.storage.getReferenceFromUrl("gs://petstagram-2e298.appspot.com")

    lateinit var statedCategory: Category

    private val _posts = MutableStateFlow<List<Pair<String, Post>>>(emptyList())

    val posts : StateFlow<List<Pair<String, Post>>> = _posts

    private var indexesOfPosts = 10L

    fun fetchPosts(){
        viewModelScope.launch {

            _posts.collect{

                val posts = mutableListOf<Pair<String, Post>>()
                db.collection("Posts")
                    .whereEqualTo("category", statedCategory.name)
                    .limit(indexesOfPosts)
                    .orderBy("postedDate", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty){
                            for (catJson in querySnapshot.documents){
                                if(catJson.id !in _posts.value.map { it.second.id }){
                                    val castedPost = catJson.toObject(Post::class.java)

                                    storageRef.child("/PostImages/${catJson.id}").downloadUrl.addOnSuccessListener { uri ->
                                        posts.add(Pair(uri.toString(), castedPost!!))
                                    }
                                }
                            }
                        }
                    }
                delay(3000)
                _posts.value+=posts
                if (_posts.value.count().toLong() ==indexesOfPosts)
                    indexesOfPosts+=3
            }
        }
    }

}