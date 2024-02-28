package com.example.petstagram.ViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private val auth: FirebaseAuth = Firebase.auth

    private val db = Firebase.firestore

    var state: AuthUiState = AuthUiState.Base

    lateinit var localProfile: Profile

    var user by mutableStateOf("usuario@gmail.com")
        private set

    fun userTextChange(newText:String){
        user = newText
    }

    var password by mutableStateOf("contraseña")
        private set

    fun passwordTextChange(newText:String){
        password = newText
    }

    fun login(context: Context, onSuccess : ()-> Unit){
        state = AuthUiState.isLoading
        this.viewModelScope.launch {
            auth.signInWithEmailAndPassword(user, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        db.collection("Users")
                            .whereEqualTo("mail", user).get().addOnSuccessListener {
                                val user = it.first().toObject(Profile::class.java)
                                onSuccess.invoke()
                                state = AuthUiState.Success(user)
                                localProfile = user
                            }
                    }
                    else {
                        Toast.makeText(context, "credenciales incorrectos", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    fun register(context: Context, onSuccess : ()-> Unit){
        state  = AuthUiState.isLoading
        if(user.isValidEmail())
            this.viewModelScope.launch {
            auth.createUserWithEmailAndPassword(user, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        persistUser(user){
                            onSuccess.invoke()
                        }
                    }
                    else Toast.makeText(context,"credenciales incorrectos", Toast.LENGTH_SHORT).show()

                }
        }
        else Toast.makeText(context,"formato de correo no valido", Toast.LENGTH_SHORT).show()
    }

    private fun String.isValidEmail():Boolean{
        return this.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex())
    }

    fun persistUser(mail: String, onEnd: () -> Unit){
        val id = auth.currentUser!!.uid
        viewModelScope.launch {

            val profile = Profile(authId = id,id = "", mail = mail, userName = mail.split("@")[0])

            createUser(profile){onEnd.invoke()}

        }

    }


    private fun createUser(profile: Profile, onEnd: () -> Unit = {}){
        db.collection("Users")
        .whereEqualTo("userName",profile.userName)
        .get().addOnCompleteListener { usernames ->
                if (!usernames.result.isEmpty) {
                profile.userName += "1"
                createUser(profile)
                }else {
                db.collection("Users")
                    .add(profile).addOnSuccessListener {
                        profile.id = it.id
                        localProfile = profile
                        state = AuthUiState.Success(profile)
                        db.collection("Users").document(it.id).update("id", it.id)
                        onEnd.invoke()
                    }
            }
        }
    }




}