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
    var postedDate : Date = Date.from(Instant.now())

}