package com.example.petstagram.ViewModels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petstagram.UiData.Perfil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val db = Firebase.firestore

    var usuario by mutableStateOf("usuario@gmail.com")
        private set

    fun cambiaUsuario(nuevoTexto:String){
        usuario = nuevoTexto
    }

    var password by mutableStateOf("contraseña")
        private set

    fun cambiaPassword(nuevoTexto:String){
        password = nuevoTexto
    }

    fun iniciarSesion(context: Context, exito : ()-> Unit){
        this.viewModelScope.launch {
            auth.signInWithEmailAndPassword(usuario, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        exito.invoke()
                    else Toast.makeText(context,"credenciales incorrectos", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun registrarse(context: Context, exito : ()-> Unit){
        if(usuario.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()))
            this.viewModelScope.launch {
            auth.createUserWithEmailAndPassword(usuario, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        persistirUsuario(usuario)
                        exito.invoke()
                    }
                    else Toast.makeText(context,"credenciales incorrectos", Toast.LENGTH_SHORT).show()

                }
        }
    }


    fun persistirUsuario(correo: String){
        val id = auth.currentUser?.uid
        viewModelScope.launch {

            val perfil = Perfil(id = id.toString(), correo = correo, nombreUsuario = correo.split("@")[0])

            crearUsuario(perfil)

        }

    }


    private fun crearUsuario(perfil: Perfil){
        db.collection("Usuarios")
        .whereEqualTo("nombreUsuario",perfil.nombreUsuario)
        .get().addOnCompleteListener {
            if (!it.result.isEmpty) {
                perfil.nombreUsuario += "1"
                crearUsuario(perfil)
            }else
                db.collection("Usuarios")
                    .add(perfil)
        }
    }




}