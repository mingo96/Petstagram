package com.example.petstagram.ViewModels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Profile
import com.example.petstagram.data.AuthUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    /**Firebase Auth reference*/
    private val auth: FirebaseAuth = Firebase.auth

    /**Firebase Firestore reference*/
    private val db = Firebase.firestore

    /**actual UI state*/
    private val _state = MutableLiveData<AuthUiState>()

    /**visible version of [_state]*/
    var state: LiveData<AuthUiState> = _state

    /**profile we generate the user*/
    var localProfile: Profile by mutableStateOf(Profile())

    /**content for the "user" field*/
    var user by mutableStateOf("usuario@gmail.com")
        private set

    /**changes content of [user]
     * @param newText next value for [user]*/
    fun userTextChange(newText:String){
        user = newText
    }

    /**content for the "password" field*/
    var password by mutableStateOf("contraseña")
        private set

    /**changes content of [password]
     * @param newText next value for [password]*/
    fun passwordTextChange(newText:String){
        password = newText
    }

    /**executed when trying to log in, sets [_state] to [AuthUiState.IsLoading] and tries
     * to authenticate, changes [_state] depending on the result*/
    fun login(context: Context, onSuccess : ()-> Unit){

        _state.value = AuthUiState.IsLoading

        viewModelScope.launch {

            auth.signInWithEmailAndPassword(user, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        db.collection("Users")
                            .whereEqualTo("mail", user).get()
                            .addOnSuccessListener {
                                val user = it.first().toObject(Profile::class.java)
                                onSuccess.invoke()
                                _state.value = AuthUiState.Success(user)
                                localProfile = user
                            }
                    }
                    else {
                        _state.value = AuthUiState.Error
                        Toast.makeText(context, "credenciales incorrectos", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    /**executed when trying to register, sets [_state] to [AuthUiState.IsLoading] and tries
     * to authenticate, changes [_state] depending on the result*/
    fun register(context: Context, onSuccess : ()-> Unit){

        _state.value = AuthUiState.IsLoading

        if(user.isValidEmail())
            viewModelScope.launch {
            auth.createUserWithEmailAndPassword(user, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        createUser(user){
                            onSuccess.invoke()
                        }
                    }
                    else Toast.makeText(context,"credenciales no válidos", Toast.LENGTH_SHORT).show()

                }
            }
        else Toast.makeText(context,"formato de correo no valido", Toast.LENGTH_SHORT).show()
    }

    /**checks if the string could be an email using [String.matches]*/
    private fun String.isValidEmail():Boolean{
        return this.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex())
    }

    /**creates the user and persists it*/
    private fun createUser(mail: String, onEnd: () -> Unit){
        val id = auth.currentUser!!.uid
        viewModelScope.launch {

            val profile = Profile(authId = id,id = "", mail = mail, userName = mail.split("@")[0])

            persistUser(profile){
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
    private fun persistUser(profile: Profile, onEnd: () -> Unit = {}){
        db.collection("Users")
        .whereEqualTo("userName",profile.userName)
        .get().addOnCompleteListener { usernames ->
                if (!usernames.result.isEmpty) {
                profile.userName += "1"
                persistUser(profile)
                }else {
                db.collection("Users")
                    .add(profile).addOnSuccessListener {
                        profile.id = it.id
                        localProfile = profile
                        _state.value = AuthUiState.Success(profile)
                        db.collection("Users").document(it.id).update("id", it.id)
                        onEnd.invoke()
                    }
            }
        }
    }




}