package com.example.petstagram.UiData

import com.example.petstagram.like.Pressed

open class Comment (

    var user :String = "",
    var commentPost : String = "",
    var commentText :String= "",
    var likes : MutableList<Like> = mutableListOf()
)

class UIComment (
    var objectUser : Profile = Profile(),
    var liked : Pressed = Pressed.False
): Comment()