package com.example.petstagram.UiData

class Perfil (

    var id :String= "",
    var correo : String = "",
    var nombreUsuario :String= "",
    val publicaciones : MutableList<Publicacion> = mutableListOf(),
    var fotoPerfil : String = ""
){
}