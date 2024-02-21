package com.example.petstagram.UiData

class Profile (

    var id :String= "",
    var mail : String = "",
    var userName :String= "",
    val posts : MutableList<Post> = mutableListOf(),
    var profilePic : String = ""
){
}