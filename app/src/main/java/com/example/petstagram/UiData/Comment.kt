package com.example.petstagram.UiData

import com.example.petstagram.like.Pressed

class Comment (
    val user :Profile,
    val commentText :String= "",
    var like : Pressed = Pressed.False
){
    fun profilePic(): String {
        return user.profilePic
    }
}