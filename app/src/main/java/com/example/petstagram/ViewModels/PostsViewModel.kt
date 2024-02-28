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

    var ids = _posts.value.map { it.second.id }

    private var indexesOfPosts = 10L

    fun fetchPosts(){
        viewModelScope.launch {

            _posts.collect{

                val posts = mutableListOf<Pair<String, Post>>()

                db.collection("Posts")
                    .orderBy("postedDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category", statedCategory.name)
                    .limit(indexesOfPosts)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty){
                            for (postJson in querySnapshot.documents){
                                if(postJson.id !in ids){
                                    ids+=postJson.id

                                    val castedPost = postJson.toObject(Post::class.java)

                                    storageRef.child("/PostImages/${postJson.id}").downloadUrl.addOnSuccessListener { uri ->
                                        _posts.value+=(Pair(uri.toString(), castedPost!!))
                                        _posts.value = _posts.value.sortedBy { it.second.postedDate }.reversed()
                                    }
                                }
                            }
                        }
                    }
                delay(4000)

                if (_posts.value.count().toLong() >=indexesOfPosts)
                    indexesOfPosts+=10
            }

        }
    }

}