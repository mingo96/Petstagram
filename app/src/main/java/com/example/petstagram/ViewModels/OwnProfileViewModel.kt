package com.example.petstagram.ViewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.petstagram.Controllers.GeneralController
import com.example.petstagram.UiData.Pet
import com.example.petstagram.UiData.Profile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration

class OwnProfileViewModel : GeneralController(){

    /**id of our profile, to keep up with the user data*/
    var selfId by mutableStateOf("")

    /**our profile*/
    private var _selfProfile = MutableStateFlow(Profile())

    override var actualUser = _selfProfile.value
        get() {return _selfProfile.value}

    private val _state = MutableLiveData(false)

    val state : LiveData<Boolean> = _state

    private val _offset = MutableStateFlow(0.dp)

    val offset : StateFlow<Dp> = _offset

    private var isMoving by mutableStateOf(false)

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())

    val pets : StateFlow<List<Pet>> = _pets

    /**indicates if we are editing the UserName, because if we are editing and reload,
     * username will go back to original*/
    private val _isEditing = MutableLiveData(false)

    /**live data to access [_isEditing]*/
    val isEditing :LiveData<Boolean> = _isEditing

    /**new username container*/
    private var userName by mutableStateOf("")

    /**the url of the profile pic, since it can change, we need it here*/
    private var _resource = MutableLiveData<String>()

    /**visible version of [_resource]*/
    val resource :LiveData<String> = _resource


    /**gets executed once, tells [_posts] to keep collecting info from [db]
     * also orders content and sets [indexesOfPosts] for more if needed*/
    private fun fetchPosts(){
        if (!_isLoading.value!!) {
            viewModelScope.launch {

                _isLoading.value = true

                base.postsFromUser(actualUser.id)
                base.petsFromUser(actualUser.id)

                while (base.alreadyLoading){
                    delay(10)
                }

                val endPosts = base.postsFromUser(actualUser.id)

                val endPets = base.petsFromUser(actualUser.id)

                for (pet in endPets- pets.value.toSet()){
                    _pets.value += pet
                }
                for (post in endPosts- _posts.value.toSet()){
                    _posts.value += post
                    delay(500)
                }
                _isLoading.value = false

            }

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
                        db.collection("Posts").document(i.id).update("creatorUser", _selfProfile.value)
                        i.creatorUser=_selfProfile.value
                    }
                    _isEditing.value = !_isEditing.value!!
                    fetchPosts()
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
    fun setResource(uri: Uri, context: Context) {
        val isImage = getMimeType(context = context, uri)?.startsWith("image/") == true
        if (isImage) {
            storageRef.child("ProfilePictures/${_selfProfile.value.id}").putFile(uri)
                .addOnSuccessListener {
                    storageRef.child("/ProfilePictures/${_selfProfile.value.id}").downloadUrl.addOnSuccessListener {
                        _selfProfile.value.profilePic = it.toString()
                        db.collection("Users").document(_selfProfile.value.id)
                            .update("profilePic", it.toString()).addOnSuccessListener {
                                for (i in _posts.value) {
                                    db.collection("Posts").document(i.id).update("creatorUser", _selfProfile.value)
                                }
                            }

                    }
                }
        }
    }

    /**gets executed once at launch, tells [_selfProfile] to get updated every 10 secs with info in [db]
     * only updates if we are not editing, the name we're writing could be overWritten*/
    fun keepUpWithUserInfo() {
        viewModelScope.launch {
            _selfProfile
                //we make it so it doesnt load more if we get out of the app
                .stateIn(
                    viewModelScope,
                    started = SharingStarted.WhileSubscribed(10000),
                    0
                )
                .collect{

                Log.i("Profile", "loading user ${_selfProfile.value.userName} data, ${posts.value.size}")

                delay(1000)
                db.collection("Users").whereEqualTo("id", selfId).get()
                .addOnSuccessListener {

                    val newVal = it.documents[0].toObject(Profile::class.java)!!
                    if (newVal != _selfProfile.value){
                        _selfProfile.value = newVal
                    }
                    _resource.value = _selfProfile.value.profilePic
                    fetchPosts()
                }

                delay(15000)
            }

        }
    }


    override fun scroll() {
        fetchPosts()
    }

    fun clear(){
        _posts.value = emptyList()
        _offset.value = 0.dp
    }

    fun ToggleState(width : Dp){
        if (isMoving) return;
        isMoving = true
        _state.value = !_state.value!!

        viewModelScope.launch {
            val objective = if (width.value == _offset.value.value) 0.dp else width
            while (_offset.value!= objective) {
                if (objective > _offset.value) {
                    _offset.value = Dp(_offset.value.value + 50)
                }
                else if (objective < _offset.value) {
                    _offset.value = Dp(_offset.value.value - 50)
                }
                if ((objective - _offset.value).value in -60f..60f && objective != _offset.value) {
                    _offset.value = objective
                }
                delay(1)

            }
            isMoving = false
        }
    }

}