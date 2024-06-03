package com.example.petstagram.UiData

class NotificationChannel(
    var user: String = "") {
    var id = ""
    var notifications = emptyList<Notification>()
}

data class Notification(
    var type :TypeOfNotification? = null,
    var seen: Boolean = false,
    var sender: String = "",
    var userName : String = "",
    var notificationText:String = ""
)

enum class TypeOfNotification{
    Like,Comment,Follow,NewPost
}