package com.example.petstagram.UiData

import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed

class UIPost: Post() {
    var liked = Pressed.False
    var saved = SavePressed.No
}