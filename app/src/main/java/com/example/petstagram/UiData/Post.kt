package com.example.petstagram.UiData

import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed

class Post ()
{
    var id : String = ""
    var title : String = ""
    lateinit var category : Category
    var creatorUser: Profile? = null
    var saved : SavePressed = SavePressed.No
    var like : Pressed = Pressed.False

    fun profilePic(): String {
        return creatorUser!!.profilePic
    }
}