package com.example.petstagram.Controllers

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import com.example.petstagram.UiData.Comment
import com.example.petstagram.UiData.Like
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.like.Pressed
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

interface CommentsUIController {


    /**Firebase FireStore reference*/
    val db: FirebaseFirestore


    var storageRef : StorageReference

    val actualComments : LiveData<List<UIComment>>

    var actualUser : Profile

    var commenting: LiveData<Boolean>

    var commentContent : LiveData<String>
    fun likeOnComment(comment : UIComment) : Boolean{
        val newLike = Like(userId = actualUser.id)
        return if(comment.likes.find { it.userId==actualUser.id } == null) {

            comment.likes += newLike
            db.collection("Comments")
                .document(comment.id)
                .update(
                    "likes" , FieldValue.arrayUnion(newLike)
                )
            comment.liked = Pressed.True
            true
        }else{

            comment.likes.removeIf { it.userId ==actualUser.id }
            db.collection("Comments")
                .document(comment.id)
                .update(
                    "likes" , FieldValue.arrayRemove(newLike)
                )
            comment.liked = Pressed.False

            false
        }

    }

    fun textChange(newText : String)

    fun commentingToggle()

    fun selectPostForComments(post : UIPost)

    fun clearComments()

    fun comment(post: UIPost){
        val newComment =
            Comment(id = "", user = actualUser.id, commentPost = post.id, commentText = commentContent.value!!)

        db.collection("Comments").add(newComment).addOnSuccessListener {
            db.collection("Posts").document(post.id)
                .update("comments", FieldValue.arrayUnion(it.id))
            db.collection("Comments").document(it.id).update("id", it.id)

            newComment.id = it.id
            post.comments.add(newComment.id)

            selectPostForComments(post)

        }
        commentingToggle()
    }



}