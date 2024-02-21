package com.example.petstagram.UiData

class Category (
    val name :String,
    var posts : MutableList<Post> = mutableListOf(),
    val categoryImage :String
){
}