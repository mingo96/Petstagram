package com.example.petstagram.ViewModels

import android.util.Log
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
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ProfileObserverViewModel : GeneralController(){

    /**id of our profile, to keep up with the user data*/
    var selfId by mutableStateOf("")

    /**our profile*/
    private var _selfProfile = MutableStateFlow(Profile())

    override var actualUser = _selfProfile.value
        get() {return _selfProfile.value}

    private val _observedProfile = MutableStateFlow(staticProfile)

    val observedProfile : StateFlow<Profile> = _observedProfile

    private val _state = MutableLiveData(false)

    val state : LiveData<Boolean> = _state

    private val _offset = MutableStateFlow(0.dp)

    val offset : StateFlow<Dp> = _offset

    private var isMoving by mutableStateOf(false)

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())

    val pets : StateFlow<List<Pet>> = _pets

    private val _follow : MutableLiveData<Boolean?> = MutableLiveData(false)

    val follow : LiveData<Boolean?> = _follow

    /**gets executed once, tells [_posts] to keep collecting info from [db]
     * also orders content and sets [indexesOfPosts] for more if needed*/
    private fun fetchPosts(){
        if (!_isLoading.value!!) {
            viewModelScope.launch {

                _observedProfile.value = staticProfile

                _follow.value = _observedProfile.value.followers.any { it == selfId }

                _isLoading.value = true

                base.postsFromUser(_observedProfile.value.id)
                base.petsFromUser(_observedProfile.value.id)

                while (base.alreadyLoading){
                    delay(100)
                }

                val endPosts =
                    base.postsFromUser(_observedProfile.value.id)

                val endPets =
                    base.petsFromUser(_observedProfile.value.id)

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


    /**gets executed once at launch, tells [_selfProfile] to get updated every 10 secs with info in [db]
     * only updates if we are not editing, the name we're writing could be overWritten*/
    fun keepUpWithUserInfo() {
        viewModelScope.launch {
            _observedProfile
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
                            fetchPosts()
                        }

                    delay(15000)
                }

        }
    }

    fun followers()= _observedProfile.value.followers.size

    fun follow(){
        animation()
        if (_observedProfile.value.followers.contains(_selfProfile.value.id)) return;

        _observedProfile.value.followers += _selfProfile.value.id
        db.collection("Users").document(_observedProfile.value.id).update("followers", FieldValue.arrayUnion(_selfProfile.value.id))
    }

    fun unFollow(){
        animation()

        if (!_observedProfile.value.followers.contains(_selfProfile.value.id)) return;
        _observedProfile.value.followers -= _selfProfile.value.id
        db.collection("Users").document(_observedProfile.value.id).update("followers", FieldValue.arrayRemove(_selfProfile.value.id))

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

    override fun scroll() {
        fetchPosts()
    }

    fun clear(){
        _posts.value= emptyList()
        _offset.value = 0.dp
        _state.value = false
        _pets.value= emptyList()
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

    companion object{
        var staticProfile : Profile= Profile()
    }

}