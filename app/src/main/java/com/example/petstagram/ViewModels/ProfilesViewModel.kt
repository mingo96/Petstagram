package com.example.petstagram.ViewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfilesViewModel : ViewModel() {

    val db = Firebase.firestore

    val storageRef = Firebase.storage.reference

    var selfId = ""

    var _selfProfile = MutableStateFlow<Profile>(Profile())

    private val _isloading = MutableLiveData(true)

    val isLoading : LiveData<Boolean> = _isloading

    private val _isEditing = MutableLiveData(false)
    val isEditing :LiveData<Boolean> = _isEditing

    var userName by mutableStateOf("")

    private val _posts = MutableStateFlow<List<Pair<String, Post>>>(emptyList())

    val posts : StateFlow<List<Pair<String, Post>>> = _posts

    var ids = _posts.value.map { it.second.id }

    private var indexesOfPosts = 10L

    private var _resource = MutableLiveData<String>()

    val resource :LiveData<String> = _resource


    fun fetchPosts(){
        viewModelScope.launch {

            _posts.collect{

                if (!_isEditing.value!!) {
                    //_posts va recolectando de la coleccion Posts
                    getFirebasePosts()
                }
                delay(4000)
                _isloading.value = (_posts.value.isEmpty())
                if (_posts.value.count().toLong() >=indexesOfPosts)
                    indexesOfPosts+=10
            }

        }
    }

    fun editUserNameClicked(context : Context){
        if (!_isEditing.value!!){
            userName = _selfProfile.value.userName.toString()
            _isEditing.value = !_isEditing.value!!
        }else{
            db.collection("Users").whereEqualTo("userName", userName).get().addOnSuccessListener {
                if (it.isEmpty){
                    pushNewUserName()
                }else
                    Toast.makeText(context,"nombre de usuario no disponible", Toast.LENGTH_SHORT).show()
            }


        }
    }

    fun getFirebasePosts(){
        db.collection("Posts")
            //filtros
            .whereEqualTo("creatorUser", _selfProfile.value)
            .orderBy("postedDate", Query.Direction.DESCENDING)
            //la máxima a sacar es indexesOfPosts, para no sacar cada entrada a la primera
            .limit(indexesOfPosts)
            .get()
            .addOnSuccessListener { querySnapshot ->

                if (!querySnapshot.isEmpty) {
                    //si no está vacío
                    for (postJson in querySnapshot.documents) {
                        if (postJson.id !in ids) {
                            ids += postJson.id
                            savePostAndUrl(postJson)
                        }
                    }
                }
            }
    }

    private fun savePostAndUrl(postJson : DocumentSnapshot) {
        //si no está ya en las ids
        val castedPost = postJson.toObject(Post::class.java)

        //obtenemos la url de la imagen, una vez hecho la añadimos a _posts
        storageRef.child("/PostImages/${postJson.id}")
            .downloadUrl.addOnSuccessListener { uri ->
                _posts.value += (Pair(uri.toString(), castedPost!!))
                _posts.value =
                    _posts.value.sortedBy { it.second.postedDate }
                        .reversed()
            }
    }

    fun pushNewUserName(){
        if (userName!=_selfProfile.value.userName)
            db.collection("Users")
                .document(_selfProfile.value.id).update("userName", userName)
                .addOnCompleteListener {
                    _isEditing.value = !_isEditing.value!!
                }
    }

    fun editUserName(newtext : String){
        Log.i("IOJJGHVIUGV", newtext)
        userName = newtext
    }

    fun getUserNameText():String{
        return userName
    }

    fun setResource(uri: Uri) {
        storageRef.child("ProfilePictures/${_selfProfile.value.id}").putFile(uri).addOnSuccessListener {
            storageRef.child("/ProfilePictures/${_selfProfile.value.id}").downloadUrl.addOnSuccessListener {
                _selfProfile.value.profilePic = it.toString()
                db.collection("Users").document(_selfProfile.value.id).update("profilePic", it.toString())

            }
        }
    }

    fun keepUpWithUserInfo() {
        viewModelScope.launch {
            _selfProfile.collect{
                db.collection("Users").whereEqualTo("id", selfId).get()
                    .addOnSuccessListener {
                        _selfProfile.value = it.documents[0].toObject(Profile::class.java)!!
                        _resource.value = _selfProfile.value.profilePic
                    }

                delay(10000)
            }
        }
    }

}