package com.example.petstagram.ViewModels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage

class PublishViewModel : ViewModel() {

    val auth = Firebase.auth

    val storage = Firebase.storage

    var storageRef = storage.reference

    lateinit var user : Profile

    lateinit var category : Category

    private val db = Firebase.firestore

    var postTitle by mutableStateOf("Titulo Publicación")
    private var _resource = MutableLiveData(Uri.EMPTY)
    var resource :LiveData<Uri> =_resource
    var postCategory by mutableStateOf("")

    val toleratedFormat =
        arrayOf("JPEG", "PNG", "GIF", "TIFF", "BMP", "RAW","MP4", "AVI", "MKV", "FLV", "MOV", "WMV")



    fun changeTitle(input:String){
        postTitle = input
    }

    fun getTitle(): String {
        return postTitle
    }

    fun postPost(onSuccess : ()->Unit){
        val isVideo = uriFormat().contains("video")
        val isImage = uriFormat().contains("image")
        if (resource.value!= null &&
            resource.value != Uri.EMPTY &&
            (isVideo||isImage) &&
            postTitle!= "Titulo Publicacion") {
                val newPost = Post()
                newPost.title = postTitle
                newPost.category = category.name
                newPost.typeOfMedia = if (isImage) "image" else if(isVideo) "video" else "unknown"
                newPost.creatorUser = user
                db.collection("Posts")
                    .add(newPost).addOnSuccessListener {
                        pushResource(it.id)
                        db.collection("Posts").document(it.id).update("id", it.id)
                        postTitle = "Titulo Publicacion"
                        _resource.value = Uri.EMPTY
                    }
            onSuccess.invoke()
        }
    }

    fun uriFormat(): String {
        return resource.value.toString().split("/").last().split(".").last()
    }

    fun pushResource(id:String): UploadTask {
        return storageRef.child("PostImages/$id").putFile(_resource.value!!)
    }

    fun setResource(uri: Uri?) {
        _resource.value = uri
    }


}