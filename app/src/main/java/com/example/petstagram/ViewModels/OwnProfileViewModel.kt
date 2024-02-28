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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OwnProfileViewModel : ViewModel() {

    /**Firebase FireStore reference*/
    private val db = Firebase.firestore

    /**Firebase Storage reference*/
    private val storageRef = Firebase.storage.reference

    /**id of our profile, to keep up with the user data*/
    var selfId by mutableStateOf("")

    /**our profile*/
    private var _selfProfile = MutableStateFlow(Profile())

    /**indicates still loading the first bunch of posts*/
    private val _isloading = MutableLiveData(true)

    /**live data for [_isloading]*/
    val isLoading : LiveData<Boolean> = _isloading

    /**indicates if we are editing the UserName, because if we are editing and reload,
     * username will go back to original*/
    private val _isEditing = MutableLiveData(false)

    /**live data to access [_isEditing]*/
    val isEditing :LiveData<Boolean> = _isEditing

    /**new username container*/
    private var userName by mutableStateOf("")

    /**mutable state of posts and their content url, separated because it can change of location
     * but not in the [Post] itself*/
    private val _posts = MutableStateFlow<List<Pair<String, Post>>>(emptyList())

    /**visible version of [_posts]*/
    val posts : StateFlow<List<Pair<String, Post>>> = _posts

    /**keeps the ids of posts we already have*/
    private var ids by mutableStateOf(_posts.value.map { it.second.id })

    /**number of posts we have, to load them progressively*/
    private var indexesOfPosts = 10L

    /**the url of the profile pic, since it can change, we need it here*/
    private var _resource = MutableLiveData<String>()

    /**visible version of [_resource]*/
    val resource :LiveData<String> = _resource


    /**gets executed once, tells [_posts] to keep collecting info from [db]
     * also orders content and sets [indexesOfPosts] for more if needed*/
    fun fetchPosts(){
        viewModelScope.launch {

            _posts.collect{

                delay(4000)
                if (!_isEditing.value!!) {
                    getFirebasePosts()
                }
                delay(4000)
                _isloading.value = (_posts.value.isEmpty())
                if (_posts.value.count().toLong() >= indexesOfPosts)
                    indexesOfPosts += 10
                //order the list
                _posts.value = _posts.value.sortedBy { it.second.postedDate }
                .reversed()

            }


        }
    }


    /**gets the posts JSON filtering by [_selfProfile]*/
    private fun getFirebasePosts(){
        db.collection("Posts")
            //filters
            .whereEqualTo("creatorUser", _selfProfile.value)
            .orderBy("postedDate", Query.Direction.DESCENDING)
            //max amount is indexesOfPosts, to get them progressively TODO make it increment when needed
            .limit(indexesOfPosts)
            .get()
            .addOnSuccessListener { querySnapshot ->

                if (!querySnapshot.isEmpty) {
                    //case not empty
                    for (postJson in querySnapshot.documents) {
                        //case we dont have this id yet
                        if (postJson.id !in ids) {
                            ids += postJson.id
                            savePostAndUrl(postJson)
                        }
                    }
                }
            }
    }

    /**given a JSON, saves its [Post] info and its url*/
    private fun savePostAndUrl(postJson : DocumentSnapshot) {

        val castedPost = postJson.toObject(Post::class.java)

        storageRef.child("/PostImages/${postJson.id}")
            .downloadUrl.addOnSuccessListener { uri ->
                _posts.value += (Pair(uri.toString(), castedPost!!))


            }
    }

    /**gets executed when we click te button for editing [userName]*/
    fun editUserNameClicked(context : Context){
        if (!_isEditing.value!!){
            //if we were not editing, just set the username to the profile one and set _isEditing to true
            userName = _selfProfile.value.userName
            _isEditing.value = !_isEditing.value!!
        }else{
            //if editing already, searches for any profile with the given username
            db.collection("Users").whereEqualTo("userName", userName).get().addOnSuccessListener {
                if (it.isEmpty){
                    //case there's not someone with that username, we push the Update the username
                    pushNewUserName()
                }else
                //case someone already has this name, we show it with a toast
                    Toast.makeText(context,"nombre de usuario no disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**if [userName] is valid, updates the username of this [_selfProfile]*/
    private fun pushNewUserName(){
        if (userName!=_selfProfile.value.userName) {
            _selfProfile.value.userName = userName
            db.collection("Users")
                .document(_selfProfile.value.id).update("userName", userName)
                .addOnCompleteListener {
                    //i don't think im supposed to need to do this but it doesn't work if i dont
                    for (i in _posts.value) {
                        db.collection("posts").document(i.second.id).update("creatorUser", _selfProfile.value)
                    }
                    _isEditing.value = !_isEditing.value!!
                }
        }
    }

    /**edit [userName]
     * @param newtext text that is going to [userName]]*/
    fun editUserName(newtext : String){
        userName = newtext
    }

    /**get [userName]
     * @return actual value of [userName]*/
    fun getUserNameText():String{
        return userName
    }

    /**pushes the given [uri], gets its download link and persists it to the [db] and [_selfProfile]
     * @param uri the local [Uri] of the stated file*/
    fun setResource(uri: Uri) {
        storageRef.child("ProfilePictures/${_selfProfile.value.id}").putFile(uri).addOnSuccessListener {
            storageRef.child("/ProfilePictures/${_selfProfile.value.id}").downloadUrl.addOnSuccessListener {
                _selfProfile.value.profilePic = it.toString()
                db.collection("Users").document(_selfProfile.value.id).update("profilePic", it.toString())

            }
        }
    }

    /**gets executed once at launch, tells [_selfProfile] to get updated every 10 secs with info in [db]
     * only updates if we are not editing, the name we're writing could be overWritten*/
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