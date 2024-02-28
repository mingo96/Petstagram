package com.example.petstagram.UiData

import com.example.petstagram.like.Pressed

class Comment (
){
    var user :Profile = Profile()
    var commentPost : Post = Post()
    var commentText :String= ""
    var like : Pressed = Pressed.False
    fun profilePic(): String {
        return user.profilePic
    }
}