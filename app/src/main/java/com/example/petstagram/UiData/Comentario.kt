package com.example.petstagram.UiData

import com.example.petstagram.like.Pulsado

class Comentario (
    val usuario :Perfil,
    val textoComentario :String= "",
    var meGusta : Pulsado = Pulsado.False
){
    fun fotoPerfil(): String {
        return usuario.fotoPerfil
    }
}