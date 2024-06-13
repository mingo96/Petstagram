package com.example.petstagram.ViewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.NotificationChannel
import com.example.petstagram.UiData.Profile
import com.example.petstagram.data.AuthUiState
import com.example.petstagram.notifications.PetstagramNotificationsService
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**[ViewModel] for the in-app authentication, works with plain e-mail and Google auth*/
class AuthViewModel : ViewModel() {

    /**Firebase Auth reference*/
    val auth: FirebaseAuth = Firebase.auth

    /**Firebase Firestore reference*/
    private val db = Firebase.firestore

    /**actual UI state*/
    private val _state = MutableLiveData<AuthUiState>()

    /**visible version of [_state]*/
    var state: LiveData<AuthUiState> = _state

    /**`true` if the user is trying to register, `false` if trying to log in*/
    private val _registering = MutableLiveData(true)

    /**visible version of [_registering]*/
    val registering: LiveData<Boolean> = _registering

    /**`true` if the info button has been clicked 10 seconds ago or less*/
    private val _helpDisplayed = MutableLiveData(false)

    val userIsNew by mutableStateOf(auth.currentUser == null)

    /**visible version of [_helpDisplayed]*/
    val helpDisplayed: LiveData<Boolean> = _helpDisplayed

    /**profile we generate for the user*/
    var localProfile: Profile? by mutableStateOf(null)

    /**content for the [Profile.mail] field*/
    var user by mutableStateOf("")
        private set

    /**changes content of [user]
     * @param newText next value for [user]*/
    fun userTextChange(newText: String) {
        user = newText
    }

    /**content for the "password" field*/
    var password by mutableStateOf("")
        private set

    /**changes content of [password]
     * @param newText next value for [password]*/
    fun passwordTextChange(newText: String) {
        password = newText
    }

    /**executed when trying to log in, sets [_state] to [AuthUiState.IsLoading] and tries
     * to authenticate, changes [_state] depending on the result*/
    fun login(context: Context, onSuccess: () -> Unit) {

        if (!user.isValidEmail()) {
            Toast.makeText(context, "Corrreo no válido", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isBlank() || password.length < 5) {
            Toast.makeText(context, "Contraseña no válida", Toast.LENGTH_SHORT).show()
            return
        }
        _state.value = AuthUiState.IsLoading

        viewModelScope.launch {

            auth.signInWithEmailAndPassword(user, password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    db.collection("Users").whereEqualTo("mail", user).get().addOnSuccessListener {
                        val user = it.first().toObject(Profile::class.java)
                        onSuccess.invoke()
                        _state.value = AuthUiState.Success(user)
                        localProfile = user

                        db.collection("NotificationsChannels").whereEqualTo("user", user.id).get()
                            .addOnSuccessListener {
                                if (it.isEmpty) {
                                    createNotificationsChannel(localProfile!!)
                                }
                            }
                        context.stopService(
                            Intent(
                                context, PetstagramNotificationsService::class.java
                            )
                        )

                        context.startForegroundService(
                            Intent(
                                context, PetstagramNotificationsService::class.java
                            ).putExtra(
                                "id", localProfile!!.id
                            )
                        )

                    }
                } else {
                    _state.value = AuthUiState.Error
                    Toast.makeText(context, "credenciales incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**executed when trying to register, sets [_state] to [AuthUiState.IsLoading] and tries
     * to authenticate, changes [_state] depending on the result*/
    fun register(context: Context, onSuccess: () -> Unit) {


        if (!user.isValidEmail()) {
            Toast.makeText(context, "Corrreo no válido", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isBlank() || password.length < 5) {
            Toast.makeText(context, "Contraseña no válida", Toast.LENGTH_SHORT).show()
            return
        }
        _state.value = AuthUiState.IsLoading

        if (user.isValidEmail()) viewModelScope.launch {
            auth.createUserWithEmailAndPassword(user, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createUser(user, context) {
                        onSuccess.invoke()
                    }
                } else {
                    _state.value = AuthUiState.Error
                    Toast.makeText(context, "credenciales no válidos", Toast.LENGTH_SHORT).show()
                }

            }
        }
        else Toast.makeText(context, "formato de correo no valido", Toast.LENGTH_SHORT).show()
    }

    /**checks if the string could be an email using [String.matches]*/
    private fun String.isValidEmail(): Boolean {
        return this.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex())
    }

    /**creates the user given its data and persists it*/
    private fun createUser(
        mail: String, context: Context, profilePic: String = "empty", onEnd: () -> Unit
    ) {
        val id = auth.currentUser!!.uid
        viewModelScope.launch {

            val profile = Profile(
                authId = id,
                id = "",
                mail = mail,
                userName = mail.split("@")[0],
                profilePic = profilePic
            )

            persistUser(profile, context) {
                onEnd.invoke()
            }

        }

    }

    /**!!!recursive function!!!
     * if the [Profile.userName] is already registered, calls itself once again but editing the name
     * with a "1" at the end
     * if not, just registers it and updates the [Profile.id] in the [db] to the id of the generated
     * document
     * @param profile profile to persist
     * @param onEnd code to execute ***when success in persisting****/
    private fun persistUser(profile: Profile, context: Context, onEnd: () -> Unit = {}) {
        db.collection("Users").whereEqualTo("userName", profile.userName).get()
            .addOnCompleteListener { usernames ->
                if (!usernames.result.isEmpty) {
                    profile.userName += "1"
                    persistUser(profile, context) {
                        onEnd()
                    }
                } else {
                    db.collection("Users").add(profile).addOnSuccessListener {
                        profile.id = it.id
                        localProfile = profile
                        _state.value = AuthUiState.Success(profile)
                        db.collection("Users").document(it.id).update("id", it.id)
                        createNotificationsChannel(localProfile!!) {
                            onEnd()

                            context.stopService(
                                Intent(
                                    context, PetstagramNotificationsService::class.java
                                )
                            )

                            context.startForegroundService(
                                Intent(
                                    context, PetstagramNotificationsService::class.java
                                ).putExtra(
                                    "id", localProfile!!.id
                                )
                            )
                        }
                    }
                }
            }
    }

    private fun createNotificationsChannel(profile: Profile, onComplete: () -> Unit = {}) {
        val notificationChannel = NotificationChannel(profile.id)
        db.collection("NotificationsChannels").add(notificationChannel).addOnSuccessListener {
            notificationChannel.id = it.id
            profile.notificationChannel = it.id
            db.collection("NotificationsChannels").document(it.id).update("id", it.id)
            db.collection("Users").document(profile.id).set(profile)
            updateProfileFromPosts(profile)
            onComplete()
        }
    }

    private fun updateProfileFromPosts(profile: Profile) {
        db.collection("Posts").whereEqualTo("creatorUser.id", profile.id).get()
            .addOnSuccessListener {
                for (i in it.documents) {
                    db.collection("Posts").document(i.id).update("creatorUser", profile)
                }
            }
    }

    /**function to recover the [Profile] given that we have the [auth] from last time*/
    fun loadUserFromAuth(context: Context, onFail: () -> Unit = {}) {
        db.collection("Users").whereEqualTo("authId", auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                try {

                    val user = it.first().toObject(Profile::class.java)
                    Log.i(user.authId, auth.currentUser!!.uid)
                    localProfile = user
                    context.stopService(Intent(context, PetstagramNotificationsService::class.java))
                    context.startForegroundService(
                        Intent(context, PetstagramNotificationsService::class.java).putExtra(
                            "id", localProfile!!.id
                        )
                    )

                } catch (e: Exception) {
                    onFail()
                }
            }
    }

    /**executes when the help button is clicked, after 10 seconds it hides again*/
    fun clickHelp() {
        if (_helpDisplayed.value!!) {
            return
        }
        viewModelScope.launch {
            _helpDisplayed.value = true
            delay(10000)
            _helpDisplayed.value = false
        }
    }

    /**executes when the type of the authentication is changed, changes [_registering]*/
    fun toggleAuthType() {
        _registering.value = !_registering.value!!
    }

    /**given the credentials, tries to find the user in the db, if not found, registers it, if found, just get it to local*/
    fun signInWithGoogleCredential(
        credential: AuthCredential,
        context: Context,
        onLogin: () -> Unit = {},
        onRegister: () -> Unit
    ) = viewModelScope.launch {
        try {
            _state.value = AuthUiState.IsLoading
            auth.signInWithCredential(credential).addOnSuccessListener { authUser ->
                db.collection("Users").whereEqualTo("mail", auth.currentUser!!.email!!).get()
                    .addOnSuccessListener {
                        if (it.isEmpty) {
                            createUser(
                                auth.currentUser!!.email!!,
                                context,
                                authUser.user!!.photoUrl.toString()
                            ) {
                                onLogin()
                                onRegister()

                            }
                        } else {

                            localProfile = it.first().toObject(Profile::class.java)
                            _state.value = AuthUiState.Success(localProfile!!)

                            onLogin()
                            db.collection("NotificationsChannels")
                                .whereEqualTo("user", localProfile!!.id).get()
                                .addOnSuccessListener {
                                    if (it.isEmpty) {
                                        createNotificationsChannel(localProfile!!)
                                    }
                                }
                            context.stopService(
                                Intent(
                                    context, PetstagramNotificationsService::class.java
                                )
                            )

                            context.startForegroundService(
                                Intent(
                                    context, PetstagramNotificationsService::class.java
                                ).putExtra(
                                    "id", localProfile!!.id
                                )
                            )
                        }
                    }


            }
        } catch (e: Exception) {

        }
    }

    fun startLoading() {
        _state.value = AuthUiState.IsLoading
    }

    fun stopLoading() {
        _state.value = AuthUiState.Error
    }
}