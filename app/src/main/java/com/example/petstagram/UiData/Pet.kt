package com.example.petstagram.UiData

import android.net.Uri

open class Pet {
    var id: String = ""
    var name = ""
    var owner = ""
    var profilePic: String = "empty"
    var category: Category? = null
    var followers: List<String> = emptyList()
}

class UIPet : Pet() {
    lateinit var uiOwner: Profile
}