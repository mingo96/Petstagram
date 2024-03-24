package com.example.petstagram.Controllers

import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIPost
import kotlinx.coroutines.flow.StateFlow

interface PostsUIController {

    val posts : StateFlow<List<UIPost>>

    var actualUser : Profile

    fun scroll(scrolled:Double)

    fun likeClicked(post:Post):Boolean

    fun saveClicked(post:Post):Boolean

}