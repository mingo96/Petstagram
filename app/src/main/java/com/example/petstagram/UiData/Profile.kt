package com.example.petstagram.UiData

class Profile(
    /**id of the auth of this user*/
    var authId: String = "",
    /**id of this document in the DB*/
    var id: String = "",
    /**mail of the user, still not used*/
    var mail: String = "",
    /**username of the user*/
    var userName: String = "",
    /**url of the photo for this account*/
    var profilePic: String = "empty",
    var reportScore: Double = 1.0,
    var followers: List<String> = emptyList(),
    var notificationChannel: String = ""
)
