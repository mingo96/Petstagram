package com.example.petstagram.Controllers

import com.example.petstagram.UiData.Comment
import com.example.petstagram.UiData.Like
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.SavedList
import com.example.petstagram.UiData.UIPost
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.StateFlow

interface PostsUIController {

    val posts : StateFlow<List<UIPost>>

    var actualUser : Profile

    /**Firebase FireStore reference*/
    val db: FirebaseFirestore
    fun scroll(scrolled:Double)

    fun likeClicked(post:Post):Boolean{
        val newLike = Like(userId = actualUser.id)
        return if(post.likes.find { it.userId==actualUser.id } == null) {

            post.likes += newLike
            db.collection("Posts")
                .document(post.id)
                .update("likes", FieldValue.arrayUnion(newLike))

            true
        }else{

            post.likes.removeIf { it.userId ==actualUser.id }
            db.collection("Posts")
                .document(post.id)
                .update("likes", FieldValue.arrayRemove(newLike))

            false
        }
    }

    fun saveClicked(post:Post):Boolean{

        val newSaved = post.id
        db.collection("SavedLists")
            .whereEqualTo("userId", actualUser.id)
            .get().addOnSuccessListener {
                if (it.isEmpty){
                    val newList = SavedList(userId=actualUser.id)
                    newList.postList.add(post.id)
                    db.collection("SavedLists").add(newList).addOnSuccessListener {document->
                        db.collection("SavedLists").document(document.id).update("docid",document.id )
                    }
                }else{
                    val thisList = it.first().toObject(SavedList::class.java)
                    if(thisList.postList.contains(post.id)) {
                        db.collection("SavedLists").document(thisList.docid).update("postList", FieldValue.arrayRemove(newSaved))
                    }else{
                        db.collection("SavedLists").document(thisList.docid).update("postList", FieldValue.arrayUnion(newSaved))
                    }
                }
            }
        return true
    }

    fun comment(content : String, post: Post):Boolean{
        if (content.length>=50){
            return false
        }
        val newComment = Comment(actualUser.id, post.id, content)
        db.collection("Posts").document(post.id).update("comments", FieldValue.arrayUnion(newComment))
        post.comments+=newComment
        return true
    }



}