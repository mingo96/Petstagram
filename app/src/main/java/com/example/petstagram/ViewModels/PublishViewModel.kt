package com.example.petstagram.ViewModels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.petstagram.UiData.Category
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage

class PublishViewModel : ViewModel() {


    /**Firebase Storage reference, since it will only push, doesnt need a route*/
    private var storageRef = Firebase.storage.reference

    /**[Profile] of the user posting*/
    lateinit var user : Profile

    /**[Category] for the [Post]*/
    lateinit var category : Category

    /**Firebase Reference*/
    private val db = Firebase.firestore

    /**Title the [Post] will have*/
    private var postTitle by mutableStateOf("Titulo Publicación")

    /**[Uri] the resource of the [Post] will have*/
    private var _resource = MutableLiveData(Uri.EMPTY)

    /**[LiveData] for [_resource]*/
    var resource :LiveData<Uri> =_resource

    /**changes [postTitle]
     * @param input new value for [postTitle]*/
    fun changeTitle(input:String){
        postTitle = input
    }

    /**gives the info in [postTitle]
     * @return the actual value of [postTitle]*/
    fun getTitle(): String {
        return postTitle
    }

    /**posts a [Post] with the info we have (if it is valid)*/
    fun postPost(onSuccess : ()->Unit){
        //gotta make sure we dont get any document or strange file, atleast one has to be true
        val isVideo = resource.value!!.uriFormat().contains("video")
        val isImage = resource.value!!.uriFormat().contains("image")

        if (resource.value!= null &&
            resource.value != Uri.EMPTY &&
            (isVideo||isImage) &&
            postTitle!= "Titulo Publicacion") {

            //creates a post with given info
            val newPost = Post()
            newPost.title = postTitle
            newPost.category = category
            newPost.typeOfMedia = if (isImage) "image" else "video"
            newPost.creatorUser = user

            persistPost(newPost, onSuccess)
        }
    }

    /**sends the created [Post] to the [db]
     * @param post the post about to be persisted
     * @param onSuccess code given for execution once we're finished*/
    private fun persistPost(post: Post, onSuccess: () -> Unit){

        db.collection("Posts")
            .add(post).addOnSuccessListener {
                pushResource(it.id)
                db.collection("Posts").document(it.id).update("id", it.id)
                postTitle = "Titulo Publicacion"
                _resource.value = Uri.EMPTY
                onSuccess.invoke()
            }
    }

    /**local function created to get the [Uri] extension*/
    private fun Uri.uriFormat(): String {
        return this.toString().split("/").last().split(".").last()
    }

    /**simply sends the file in [_resource]
     * @param id the name the file will have (same as post)*/
    private fun pushResource(id:String): UploadTask {
        return storageRef.child("PostImages/$id").putFile(_resource.value!!)
    }

    /**setter for resource*/
    fun setResource(uri: Uri?) {
        _resource.value = uri
    }


}