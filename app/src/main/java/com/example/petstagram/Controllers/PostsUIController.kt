package com.example.petstagram.Controllers

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.petstagram.UiData.Comment
import com.example.petstagram.UiData.Like
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.Report
import com.example.petstagram.UiData.SavedList
import com.example.petstagram.UiData.UIComment
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface PostsUIController:CommentsUIController {

    val posts : StateFlow<List<UIPost>>

    val isLoading : LiveData<Boolean>

    val funnyAhhString : StateFlow<String>

    fun startRollingDots()

    fun scroll()

    fun likeOnPost(post:UIPost) {
        val newLike = Like(userId = actualUser.id)
        if(!post.likes.any { it.userId==actualUser.id }) {

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

    fun reportPost(post: UIPost, context: Context){
        if (post.reports.any { it.user==actualUser.id }) {
            Toast.makeText(context,"Ya has reportado este post!", Toast.LENGTH_LONG).show()
            return;
        }

        post.reports.add(Report().apply {
            user = actualUser.id
            score = actualUser.reportScore
        })

        if(post.reports.sumOf { it.score } >= 10){
            db.collection("Posts").document(post.id).delete()
        }else{
            db.collection("Posts").document(post.id).update("reports", post.reports)
        }

    }

    fun savePostResource(post: UIPost, context: Context){
        try {
            var routeToDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

            val destination = File(routeToDownloads,post.id +"."+ if (post.typeOfMedia == "video") "mp4" else "jpeg")

            if (!destination.createNewFile()){
                Toast.makeText(context,"Ya has bajado este post!", Toast.LENGTH_LONG).show()
                return
            }

            storageRef.child("PostImages/${post.id}").getFile(destination).addOnCompleteListener{
                if (it.isCanceled)
                    Toast.makeText(context,"Algo salió mal", Toast.LENGTH_LONG).show()
                if (it.isSuccessful)
                    Toast.makeText(context,"Archivo bajado", Toast.LENGTH_LONG).show()
            }
        }catch (e:Exception){
            Toast.makeText(context,"Ya has bajado este post!", Toast.LENGTH_LONG).show()
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