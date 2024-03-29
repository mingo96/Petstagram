package com.example.petstagram.UiData

import com.example.petstagram.like.Pressed

open class Comment (

    var id : String = "",
    var user :String = "",
    var commentPost : String = "",
    var commentText :String= "",
    var likes : MutableList<Like> = mutableListOf()
)

class UIComment (
    var objectUser : Profile = Profile(),
    var liked : Pressed = Pressed.False
): Comment(){
    constructor(other : Comment):this(){
        id = other.id
        user = other.user
        commentPost = other.commentPost
        commentText = other.commentText
        likes = other.likes
    }
}