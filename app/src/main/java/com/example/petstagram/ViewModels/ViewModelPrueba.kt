package com.example.petstagram.ViewModels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
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



    fun iniciarSesion(usuario : String, password : String, context: Context, exito : ()-> Unit){
        this.viewModelScope.launch {
            auth.signInWithEmailAndPassword(usuario, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        exito.invoke()
                    else Toast.makeText(context,"credenciales incorrectos", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun registrarse(usuario : String, password : String, context: Context, exito : ()-> Unit){
        if(usuario.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()))
        this.viewModelScope.launch {
            auth.createUserWithEmailAndPassword(usuario, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        exito.invoke()
                }
        }
        else Toast.makeText(context,"credenciales incorrectos", Toast.LENGTH_SHORT).show()
    }




}