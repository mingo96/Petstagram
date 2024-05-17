package com.example.petstagram.UiData

import android.net.Uri

open class Pet {
    var id : String=""
    var name = ""
    var owner =""
    var profilePic : String = ""
    var category : Category? = null

}

class UIPet : Pet(){
    lateinit var uiOwner : Profile
}