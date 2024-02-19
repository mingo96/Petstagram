package com.example.petstagram.UiData

import com.example.petstagram.guardar.GuardarPulsado
import com.example.petstagram.like.Pulsado

class Publicacion (
    val usuarioCreador:Perfil,
    val comentarios :MutableList<Comentario> = mutableListOf(),
    val recursoPublicacion :String= "",
    var guardada : GuardarPulsado = GuardarPulsado.No,
    var meGusta : Pulsado = Pulsado.False
    )
{
    fun fotoPerfil(): String {
        return usuarioCreador.fotoPerfil
    }
}