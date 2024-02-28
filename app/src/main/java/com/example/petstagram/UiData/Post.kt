package com.example.petstagram.UiData

import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed
import java.time.Instant
import java.util.Date

class Post ()
{

    var id : String = ""
    var title : String = ""
    var category =""
    var creatorUser: Profile? = null
    var typeOfMedia:String = ""
    var saved : SavePressed = SavePressed.No
    var like : Pressed = Pressed.False
    var postedDate : Date = Date.from(Instant.now())

    fun profilePic(): String {
        return creatorUser!!.profilePic
    }
}