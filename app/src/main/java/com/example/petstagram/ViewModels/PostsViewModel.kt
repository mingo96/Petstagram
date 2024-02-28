package com.example.petstagram.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PostsViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val storageRef = Firebase.storage.getReferenceFromUrl("gs://petstagram-2e298.appspot.com")

    lateinit var statedCategory: Category

    private val _isloading = MutableLiveData(true)

    val isLoading :LiveData<Boolean> = _isloading

    val locallySaved = mutableMapOf<Category, List<Pair<String,Post>>>()

    private val _posts = MutableStateFlow<List<Pair<String, Post>>>(emptyList())

    val posts : StateFlow<List<Pair<String, Post>>> = _posts

    var ids = _posts.value.map { it.second.id }

    private var indexesOfPosts = 10L

    fun startLoadingPosts(){
        viewModelScope.launch {

            if (locallySaved.contains(statedCategory)){
                _posts.value = locallySaved[statedCategory]!!
                indexesOfPosts = _posts.value.count().toLong()
            }else{
                indexesOfPosts = 10L
                locallySaved[statedCategory] = _posts.value
            }

            _posts.collect{


                //_posts va recolectando de la coleccion Posts
                db.collection("Posts")
                    //filtros
                    .orderBy("postedDate", Query.Direction.DESCENDING)
                    .whereEqualTo("category", statedCategory)
                    //la máxima a sacar es indexesOfPosts, para no sacar cada entrada a la primera
                    .limit(indexesOfPosts)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty){
                            //si no está vacío
                            for (postJson in querySnapshot.documents){
                                if(postJson.id !in ids){
                                    //si no está ya en las ids
                                    ids+=postJson.id

                                    val castedPost = postJson.toObject(Post::class.java)

                                    //obtenemos la url de la imagen, una vez hecho la añadimos a _posts
                                    storageRef.child("/PostImages/${postJson.id}").downloadUrl.addOnSuccessListener { uri ->
                                        _posts.value+=(Pair(uri.toString(), castedPost!!))
                                        _posts.value = _posts.value.sortedBy { it.second.postedDate }.reversed()
                                        locallySaved[statedCategory] = _posts.value
                                    }
                                }
                            }
                        }
                    }
                delay(4000)
                _isloading.value = (_posts.value.isEmpty())
                if (_posts.value.count().toLong() >=indexesOfPosts)
                    indexesOfPosts+=10
            }

        }
    }


}