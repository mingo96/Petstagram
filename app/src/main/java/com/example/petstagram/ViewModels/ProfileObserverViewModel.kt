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
import com.example.petstagram.Controllers.ProfileInteractor
import com.example.petstagram.UiData.Notification
import com.example.petstagram.UiData.Pet
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.TypeOfNotification
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ProfileObserverViewModel : GeneralController(), ProfileInteractor {

    /**id of our profile, to keep up with the user data*/
    var selfId by mutableStateOf("")

    /**our profile*/
    private var _selfProfile = MutableStateFlow(Profile())

    override var actualUser = _selfProfile.value
        get() {return _selfProfile.value}

    private val _observedProfile = MutableStateFlow(staticProfile)

    val observedProfile : StateFlow<Profile> = _observedProfile

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

                _follow.value = _observedProfile.value.followers.contains(_selfProfile.value.id)

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

                    db.collection("Users").document(selfId).get()
                        .addOnSuccessListener {

                            val newVal = it.toObject(Profile::class.java)!!
                            if (newVal.id != _selfProfile.value.id){
                                _selfProfile.value = newVal
                            }
                        }
                    db.collection("Users").document(staticProfile.id).get()
                        .addOnSuccessListener {

                            val newVal = it.toObject(Profile::class.java)!!
                            if (newVal != staticProfile || newVal != _observedProfile.value){
                                staticProfile= newVal
                                _observedProfile.value = newVal
                            }
                            _follow.value = _observedProfile.value.followers.contains(_selfProfile.value.id)
                            fetchPosts()
                        }

                    delay(15000)
                }

        }
    }

    override fun followers()= _observedProfile.value.followers.size

    override fun follow(){
        animation()
        if (_observedProfile.value.followers.contains(_selfProfile.value.id)) return;

        val newNotification = Notification(
            sender = actualUser.id,
            userName = actualUser.userName,
            type = TypeOfNotification.Follow
        )

        db.collection("NotificationsChannels").document(observedProfile.value.notificationChannel)
            .update("notifications", FieldValue.arrayUnion(newNotification))

        _observedProfile.value.followers += _selfProfile.value.id
        db.collection("Users").document(_observedProfile.value.id).update("followers", FieldValue.arrayUnion(_selfProfile.value.id))
    }

    override fun unFollow(){
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

    fun clean(){
        _posts.value= emptyList()
        _pets.value= emptyList()
    }
    companion object{
        var staticProfile : Profile= Profile()
    }

}