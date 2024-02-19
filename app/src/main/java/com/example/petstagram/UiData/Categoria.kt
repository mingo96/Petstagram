package com.example.petstagram.UiData

class Categoria (
    val nombre :String,
    var publicaciones : MutableList<Publicacion> = mutableListOf(),
    val imagenCategoria :String
){
}