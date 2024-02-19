package com.example.petstagram.UiData

class Perfil (

    val idUsuario :String= "",
    var nombreUsuario :String= "",
    val publicaciones : MutableList<Publicacion> = mutableListOf(),
    var fotoPerfil : String = ""
){
}