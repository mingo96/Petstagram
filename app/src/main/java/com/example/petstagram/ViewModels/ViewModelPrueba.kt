package com.example.petstagram.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Categoria
import com.example.petstagram.UiData.Perfil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ViewModelPrueba : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    fun iniciarSesion(usuario : String, password : String, exito : ()-> Unit){
        this.viewModelScope.launch {
            auth.signInWithEmailAndPassword(usuario, password)
                .addOnCompleteListener { exito.invoke() }
        }
    }

    fun registrarse(usuario : String, password : String, exito : ()-> Unit){
        this.viewModelScope.launch {
            auth.createUserWithEmailAndPassword(usuario, password)
                .addOnCompleteListener {
                    exito.invoke()
                }
        }
    }




}