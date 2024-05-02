package com.example.petstagram.Controllers

import androidx.lifecycle.LiveData
import com.example.petstagram.UiData.Comment
import com.example.petstagram.UiData.Like
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.SavedList
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.StateFlow

interface PostsUIController:CommentsUIController {

    val posts : StateFlow<List<UIPost>>

    val isLoading : LiveData<Boolean>

    fun scroll(scrolled:Double)

    fun likeOnPost(post:UIPost) {
        val newLike = Like(userId = actualUser.id)
        if(post.likes.find { it.userId==actualUser.id } == null) {

            post.likes += newLike
            db.collection("Posts")
                .document(post.id)
                .update("likes", FieldValue.arrayUnion(newLike))

            post.liked = Pressed.True
        }else{

            post.likes.removeIf { it.userId ==actualUser.id }
            db.collection("Posts")
                .document(post.id)
                .update("likes", FieldValue.arrayRemove(newLike))

            post.liked = Pressed.False
        }
    }


    fun saveClicked(post:UIPost):Boolean{

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
                    post.saved = SavePressed.Si
                }else{
                    val thisList = it.first().toObject(SavedList::class.java)
                    if(thisList.postList.contains(post.id)) {
                        db.collection("SavedLists").document(thisList.docid).update("postList", FieldValue.arrayRemove(newSaved))
                        post.saved = SavePressed.No

                    }else{
                        db.collection("SavedLists").document(thisList.docid).update("postList", FieldValue.arrayUnion(newSaved))
                        post.saved = SavePressed.Si

                    }
                }
            }
        return true
    }

}