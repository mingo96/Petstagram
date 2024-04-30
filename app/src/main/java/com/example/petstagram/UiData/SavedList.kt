package com.example.petstagram.UiData

open class SavedList (
    var userId : String="",
    var docid : String="",
    var postList : MutableList<String> = mutableListOf()
)

class UISavedList : SavedList(){
    val UIPosts : MutableList<UIPost> = emptyList<UIPost>().toMutableList()
}