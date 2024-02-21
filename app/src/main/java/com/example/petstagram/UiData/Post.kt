package com.example.petstagram.UiData

import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed

class Post (
    var id : String = "",
    val creatorUser:Profile,
    val comments :MutableList<Comment> = mutableListOf(),
    val postResource :String= "",
    var saved : SavePressed = SavePressed.No,
    var like : Pressed = Pressed.False
    )
{
    fun profilePic(): String {
        return creatorUser.profilePic
    }
}