package com.example.petstagram.ViewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.petstagram.Controllers.GeneralController
import com.example.petstagram.Controllers.ProfileInteractor
import com.example.petstagram.UiData.Pet
import com.example.petstagram.UiData.Profile
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetObserverViewModel : GeneralController(), ProfileInteractor {

    /**id of our profile, to keep up with the user data*/
    var selfId by mutableStateOf("")

    /**our profile*/
    private var _selfProfile = MutableStateFlow(Profile())

    override var actualUser = _selfProfile.value
        get() {return _selfProfile.value}

    private val _observedPet = MutableStateFlow(Pet())

    val observedPet : StateFlow<Pet> = _observedPet

    private val _petsOwner = MutableStateFlow(Profile())

    val petsOwner : StateFlow<Profile> = _petsOwner

    /**new username container*/
    private var petName by mutableStateOf("")

    /**the url of the profile pic, since it can change, we need it here*/
    private var _resource = MutableLiveData<String>("")

    /**visible version of [_resource]*/
    val resource :LiveData<String> = _resource

    /**indicates if we are editing the UserName, because if we are editing and reload,
     * username will go back to original*/
    private val _isEditing = MutableLiveData(false)

    /**live data to access [_isEditing]*/
    val isEditing :LiveData<Boolean> = _isEditing
    override fun followers()= _observedPet.value.followers.size

    private val _follow : MutableLiveData<Boolean?> = MutableLiveData(false)

    val follow : LiveData<Boolean?> = _follow

    var imOwner by mutableStateOf(false)
        private set

    override fun follow(){
        animation()
        if (_observedPet.value.followers.contains(_selfProfile.value.id)) return;

        _observedPet.value.followers += _selfProfile.value.id
        db.collection("Pets").document(_observedPet.value.id).update("followers", FieldValue.arrayUnion(_selfProfile.value.id))
    }

    override fun unFollow(){
        animation()

        if (!_observedPet.value.followers.contains(_selfProfile.value.id)) return;
        _observedPet.value.followers -= _selfProfile.value.id
        db.collection("Pets").document(_observedPet.value.id).update("followers", FieldValue.arrayRemove(_selfProfile.value.id))

    }

    private fun animation(){
        viewModelScope.launch {
            if (_follow.value!= null) {
                val pre = _follow.value!!
                if (!pre) {
                    _follow.value = null
                    delay(1000)
                }
                _follow.value = !pre
            }
        }
    }

    /**gets executed once, tells [_posts] to keep collecting info from [db]
     * also orders content and sets [indexesOfPosts] for more if needed*/
    private fun fetchPosts(){
        if (!_isLoading.value!!) {

            viewModelScope.launch {
                _isLoading.value = true

                if (_observedPet.value != staticPet) {
                    _observedPet.value = staticPet
                    _resource.value = _observedPet.value.profilePic
                }

                imOwner = _observedPet.value.owner == selfId

                base.postsFromPet(_observedPet.value)

                while (base.alreadyLoading){
                    delay(100)
                }

                val endPosts =
                    base.postsFromPet(_observedPet.value)

                for (post in endPosts- _posts.value.toSet()){
                    _posts.value += post
                    delay(500)
                }
                _isLoading.value = false

            }

        }
    }

    /**gets executed when we click te button for editing [petName]*/
    fun editUserNameClicked(context : Context){
        if (!_isEditing.value!!){
            //if we were not editing, just set the username to the profile one and set _isEditing to true
            petName = _observedPet.value.name
            _isEditing.value = !_isEditing.value!!
        }else{
            pushNewPetName()
        }
    }


    /**gets executed once at launch, tells [_selfProfile] to get updated every 10 secs with info in [db]
     * only updates if we are not editing, the name we're writing could be overWritten*/
    fun keepUpWithUserInfo() {
        viewModelScope.launch {
            _observedPet
                //we make it so it doesnt load more if we get out of the app
                .stateIn(
                    viewModelScope,
                    started = SharingStarted.WhileSubscribed(10000),
                    0
                )
                .collect{

                    Log.i("Profile", "loading user ${_selfProfile.value.userName} data, ${posts.value.size}")

                    delay(1000)
                    db.collection("Users").document(selfId).get()
                        .addOnSuccessListener {

                            val newVal = it.toObject(Profile::class.java)!!
                            if (newVal.id != _selfProfile.value.id){
                                _selfProfile.value = newVal
                            }
                        }
                    db.collection("Users").document(staticPet.owner).get()
                        .addOnSuccessListener {

                            val newVal = it.toObject(Profile::class.java)!!
                            if (newVal.id != _selfProfile.value.id){
                                _petsOwner.value = newVal
                            }
                        }
                    db.collection("Pets").document(staticPet.id).get()
                        .addOnSuccessListener {

                            val newVal = it.toObject(Pet::class.java)!!
                            if (newVal != _observedPet.value){
                                _observedPet.value = newVal
                            }

                            _follow.value = _observedPet.value.followers.contains(_selfProfile.value.id)
                            fetchPosts()
                        }

                    delay(15000)
                }

        }
    }

    /**if [petName] is valid, updates the username of this [_selfProfile]*/
    private fun pushNewPetName(){
        if (petName!=_observedPet.value.name) {
            _observedPet.value.name = petName
            db.collection("Pets")
                .document(_observedPet.value.id).update("name", petName)
                .addOnCompleteListener {
                    _isEditing.value = !_isEditing.value!!
                    for (i in _posts.value){
                        i.uiPet!!.name = petName
                    }
                    fetchPosts()
                }
        }else{
            _isEditing.value = false
        }
    }

    /**edit [userName]
     * @param newtext text that is going to [userName]]*/
    fun editPetName(newtext : String){
        petName = newtext
    }

    /**get [userName]
     * @return actual value of [userName]*/
    fun getPetNameText():String{
        return petName
    }

    /**pushes the given [uri], gets its download link and persists it to the [db] and [_selfProfile]
     * @param uri the local [Uri] of the stated file*/
    fun setResource(uri: Uri, context: Context) {
        val isImage = getMimeType(context = context, uri)?.startsWith("image/") == true
        if (isImage) {
            storageRef.child("ProfilePictures/${_observedPet.value.id}").putFile(uri)
                .addOnSuccessListener {
                    storageRef.child("/ProfilePictures/${_observedPet.value.id}").downloadUrl.addOnSuccessListener {
                        _observedPet.value.profilePic = it.toString()
                        db.collection("Pets").document(_observedPet.value.id)
                            .update("profilePic", it.toString())

                    }
                }
        }
    }

    override fun scroll() {
        fetchPosts()
    }

    fun clear(){
        _posts.value= emptyList()
        _follow.value = false
        petName = ""
        _isEditing.value = false
    }

    companion object{
        var staticPet : Pet = Pet()
    }
}