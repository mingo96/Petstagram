package com.example.petstagram.ViewModels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

class OwnProfileViewModel : GeneralController() {

    /**id of our profile, to keep up with the user data*/
    var selfId by mutableStateOf("")

    /**our profile*/
    private var _selfProfile = MutableStateFlow(Profile())

    override var actualUser = _selfProfile.value
        get() {
            return _selfProfile.value
        }

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())

    val pets: StateFlow<List<Pet>> = _pets

    /**indicates if we are editing the UserName, because if we are editing and reload,
     * username will go back to original*/
    private val _isEditing = MutableLiveData(false)

    /**live data to access [_isEditing]*/
    val isEditing: LiveData<Boolean> = _isEditing

    /**new username container*/
    private var userName by mutableStateOf("")

    /**the url of the profile pic, since it can change, we need it here*/
    private var _resource = MutableLiveData<String>()

    /**visible version of [_resource]*/
    val resource: LiveData<String> = _resource

    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())

    val profiles: StateFlow<List<Profile>> = _profiles

    private val _profilesDisplayed = MutableStateFlow<Boolean>(false)

    val profilesDisplayed: StateFlow<Boolean> = _profilesDisplayed

    fun toggleProfilesDisplayed() {
        _profilesDisplayed.value = !_profilesDisplayed.value
    }

    /**gets executed once, tells [_posts] to keep collecting info from [db]
     * also orders content and sets [indexesOfPosts] for more if needed*/
    private fun fetchPosts() {
        if (!_isLoading.value!!) {
            viewModelScope.launch {

                _isLoading.value = true

                base.postsFromUser(actualUser.id)
                base.petsFromUser(actualUser.id)

                while (base.alreadyLoading) {
                    delay(10)
                }

                val endPosts = base.postsFromUser(actualUser.id)

                val endPets = base.petsFromUser(actualUser.id)

                for (pet in endPets - pets.value.toSet()) {
                    _pets.value += pet
                }
                for (post in endPosts - _posts.value.toSet()) {
                    _posts.value += post
                    delay(500)
                }
                _isLoading.value = false

            }

        }
    }


    /**gets executed when we click te button for editing [userName]*/
    fun editUserNameClicked(context: Context) {
        if (!_isEditing.value!!) {
            //if we were not editing, just set the username to the profile one and set _isEditing to true
            userName = _selfProfile.value.userName
            _isEditing.value = !_isEditing.value!!
        } else {
            //if editing already, searches for any profile with the given username
            db.collection("Users").whereEqualTo("userName", userName).get().addOnSuccessListener {
                if (it.isEmpty) {
                    //case there's not someone with that username, we push the Update the username
                    pushNewUserName()
                }
                _isEditing.value = false
            }
        }
    }

    /**if [userName] is valid, updates the username of this [_selfProfile]*/
    private fun pushNewUserName() {
        if (userName != _selfProfile.value.userName) {
            _selfProfile.value.userName = userName
            db.collection("Users").document(_selfProfile.value.id).update("userName", userName)
            base.updateProfileToPosts(_selfProfile.value)
            fetchPosts()
        }
    }

    /**edit [userName]
     * @param newtext text that is going to [userName]]*/
    fun editUserName(newtext: String) {
        userName = newtext
    }

    /**get [userName]
     * @return actual value of [userName]*/
    fun getUserNameText(): String {
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
                                base.updateProfileToPosts(_selfProfile.value)
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
                    viewModelScope, started = SharingStarted.WhileSubscribed(10000), 0
                ).collect {

                    _selfProfile.value = base.profile()
                    _resource.value = _selfProfile.value.profilePic
                    for (i in _selfProfile.value.followers) {
                        base.getUser(i)
                    }
                    while (base.alreadyLoading) delay(100)

                    for (i in _selfProfile.value.followers) {
                        val spare = base.getUser(i)
                        if (spare != null && spare.id !in _profiles.value.map { it.id }) {
                            _profiles.value += spare
                        }
                    }
                    delay(15000)
                }

        }
    }


    override fun scroll(generatedByScroll: Boolean) {
        if (!generatedByScroll && _posts.value.isEmpty() || generatedByScroll) {
            fetchPosts()
        }
    }

    fun clean() {
        _posts.value = emptyList()
        _isEditing.value = false
    }

}