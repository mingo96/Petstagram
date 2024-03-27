package com.example.petstagram.Controllers

import android.util.Log
import com.example.petstagram.UiData.Comment
import com.example.petstagram.UiData.Like
import com.example.petstagram.UiData.Post
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.SavedList
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.UiData.UIPost
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.StateFlow

interface PostsUIController {

    val posts : StateFlow<List<UIPost>>

    var actualUser : Profile

    /**Firebase FireStore reference*/
    val db: FirebaseFirestore
    fun scroll(scrolled:Double)

    fun likeOnPost(post:Post):Boolean{
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

    fun likeOnComment(comment : Comment) : Boolean{
        val newLike = Like(userId = actualUser.id)
        return if(comment.likes.find { it.userId==actualUser.id } == null) {
            Log.i("AAAAAAAAAAAAAAAAAAAAA", "ya likeado")

            comment.likes += newLike
            db.collection("Comments")
                .document(comment.id)
                .update(
                    "likes" ,FieldValue.arrayUnion(newLike)
                )

            true
        }else{

            comment.likes.removeIf { it.userId ==actualUser.id }
            db.collection("Comments")
                .document(comment.id)
                .update(
                    "likes" ,FieldValue.arrayRemove(newLike)
                )
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

    fun comment(content : String, post: UIPost):Boolean{
        if (content.length>=50){
            return false
        }
        val newComment = Comment("",actualUser.id, post.id, content)

        db.collection("Comments").add(newComment).addOnSuccessListener {
            db.collection("Posts").document(post.id).update("comments", FieldValue.arrayUnion(it.id))
            db.collection("Comments").document(it.id).update("id", it.id)
            newComment.id = it.id
            post.UIComments+= UIComment(newComment)
        }
        return true
    }



}