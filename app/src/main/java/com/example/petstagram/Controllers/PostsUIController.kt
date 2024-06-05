package com.example.petstagram.Controllers

import android.content.Context
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener
import android.os.Environment
import android.widget.Toast
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import com.example.petstagram.UiData.Like
import com.example.petstagram.UiData.Notification
import com.example.petstagram.UiData.Pet
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.Report
import com.example.petstagram.UiData.SavedList
import com.example.petstagram.UiData.TypeOfNotification
import com.example.petstagram.UiData.UIPost
import com.example.petstagram.ViewModels.PetObserverViewModel
import com.example.petstagram.ViewModels.ProfileObserverViewModel
import com.example.petstagram.guardar.SavePressed
import com.example.petstagram.like.Pressed
import com.google.android.play.integrity.internal.f
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.StateFlow
import java.io.File


interface PostsUIController : CommentsUIController {

    val posts: StateFlow<List<UIPost>>

    val isLoading: LiveData<Boolean>

    val optionsClicked: LiveData<UIPost?>

    var navController: NavHostController

    val funnyAhhString: StateFlow<String>

    val videoStopped: LiveData<Boolean>

    var erasing: Boolean

    val videoMode: LiveData<Boolean>

    val videoIsRunning: LiveData<Boolean?>

    val likedPost: LiveData<UIPost?>

    fun startRollingDots()

    fun scroll()

    fun likeOnPost(post: UIPost) {
        val newLike = Like(userId = actualUser.id)
        if (!post.likes.any { it.userId == actualUser.id }) {

            post.likes += newLike
            db.collection("Posts")
                .document(post.id)
                .update("likes", FieldValue.arrayUnion(newLike))

            animateLike(post)
            if (actualUser.id != post.creatorUser!!.id) {
                val newNotification = Notification(
                    type = TypeOfNotification.Like,
                    userName = actualUser.userName,
                    sender = actualUser.id,
                    notificationText = post.id
                )
                db.collection("NotificationsChannels")
                    .document(post.creatorUser!!.notificationChannel)
                    .update("notifications", FieldValue.arrayUnion(newNotification))
            }
            post.liked = Pressed.True
        } else {

            post.likes.removeIf { it.userId == actualUser.id }
            db.collection("Posts")
                .document(post.id)
                .update("likes", FieldValue.arrayRemove(newLike))

            post.liked = Pressed.False
        }
    }

    fun animateVideoMode()

    fun animatePause()

    fun animateLike(post: UIPost)

    fun reportPost(post: UIPost = optionsClicked.value!!, context: Context) {
        if (!erasing && post.creatorUser!!.id == actualUser.id) {
            Toast.makeText(
                context,
                "Volver a pulsar este botón borrará la publicación",
                Toast.LENGTH_LONG
            ).show()
            erasing = true
            return;
        }

        if (erasing && post.creatorUser!!.id == actualUser.id) {
            Toast.makeText(
                context,
                "La publicación ha sido borrada, no le saldrá a nadie más, gracias por tu tiempo",
                Toast.LENGTH_LONG
            ).show()
            deletePost(post)
            return;
        }

        if (post.reports.any { it.user == actualUser.id }) {
            Toast.makeText(context, "Ya has reportado este post!", Toast.LENGTH_LONG).show()
            return;
        }

        post.reports.add(Report().apply {
            user = actualUser.id
            score = actualUser.reportScore
        })

        if (post.reports.sumOf { it.score } >= 10) {
            Toast.makeText(
                context,
                "La publicación ha sido borrada, no le saldrá a nadie más, gracias por tu tiempo",
                Toast.LENGTH_LONG
            ).show()

            deletePost(post)
        } else {
            Toast.makeText(
                context,
                "La publicación ha sido reportada, gracias por tu tiempo",
                Toast.LENGTH_LONG
            ).show()

            db.collection("Posts").document(post.id).update("reports", post.reports)
        }

    }

    fun deletePost(post: UIPost)

    fun savePostResource(post: UIPost = optionsClicked.value!!, context: Context) {
        try {
            var routeToDownloads =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath+"/Petstagram/"
            if (!File(routeToDownloads).isDirectory) {
                File(routeToDownloads).mkdir()
            }
            val destination = File(
                routeToDownloads,
                post.id + "." + if (post.typeOfMedia == "video") "mp4" else "jpeg"
            )

            if (!destination.createNewFile()) {
                Toast.makeText(context, "Ya has bajado este post!", Toast.LENGTH_LONG).show()
                return
            }

            post.UIURL.toFile().copyTo(destination,overwrite = true)

            MediaScannerConnection.scanFile(context, arrayOf<String>(routeToDownloads),
                null,
                { path, uri -> })

        } catch (e: Exception) {
            Toast.makeText(context, "Ya has bajado este post!!", Toast.LENGTH_LONG).show()
        }
    }

    fun saveClicked(post: UIPost): Boolean {

        val newSaved = post.id
        db.collection("SavedLists")
            .whereEqualTo("userId", actualUser.id)
            .get().addOnSuccessListener {
                if (it.isEmpty) {
                    val newList = SavedList(userId = actualUser.id)
                    newList.postList.add(post.id)
                    db.collection("SavedLists").add(newList).addOnSuccessListener { document ->
                        db.collection("SavedLists").document(document.id)
                            .update("docid", document.id)
                    }
                    post.saved = SavePressed.Si
                } else {
                    val thisList = it.first().toObject(SavedList::class.java)
                    if (thisList.postList.contains(post.id)) {
                        db.collection("SavedLists").document(thisList.docid)
                            .update("postList", FieldValue.arrayRemove(newSaved))
                        post.saved = SavePressed.No

                    } else {
                        db.collection("SavedLists").document(thisList.docid)
                            .update("postList", FieldValue.arrayUnion(newSaved))
                        post.saved = SavePressed.Si

                    }
                }
            }
        return true
    }

    fun optionsClicked(post: UIPost)

    fun clearOptions()

    fun enterProfile(prof: Profile) {
        if (prof.id != actualUser.id) {
            ProfileObserverViewModel.staticProfile = prof
            navController.navigate("perfilAjeno")
        } else {
            navController.navigate("perfilPropio")
        }
    }

    fun enterPetProfile(id: Pet) {
        PetObserverViewModel.staticPet = id
        navController.navigate("mascota")

    }

    fun toggleStop()
}