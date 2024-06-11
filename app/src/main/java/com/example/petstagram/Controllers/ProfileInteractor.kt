package com.example.petstagram.Controllers

interface ProfileInteractor {

    fun follow()

    fun unFollow()

    fun followers(): Int
}