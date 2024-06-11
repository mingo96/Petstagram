package com.example.petstagram.notifications

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.petstagram.R
import com.example.petstagram.UiData.Notification
import com.example.petstagram.UiData.NotificationChannel
import com.example.petstagram.UiData.Profile
import com.example.petstagram.UiData.TypeOfNotification
import com.example.petstagram.UiData.UIPost
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class PetstagramNotificationsService() : Service() {

    private var _notificationsChannel: NotificationChannel? = null

    private lateinit var notificationService: PetstagramNotificationGenerator

    private var snapshots: MutableList<ListenerRegistration> = mutableListOf()

    val db = Firebase.firestore

    private val storageRef = Firebase.storage.reference
    companion object{
        var id: String = ""
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notification =
            NotificationCompat.Builder(this, "petstagram_notifications").setContentTitle("Notificaciones de Petstagram")
                .setContentText("Estamos esperando que alguien te vea").setSmallIcon(R.drawable.logo)
                .setPriority(NotificationManager.IMPORTANCE_NONE).setAutoCancel(true)
                .setTimeoutAfter(10)
                .build()
        startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)


        val notificationChannel= android.app.NotificationChannel(
            "petstagram_notifications",
            "Petstagram",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        notificationService =
        PetstagramNotificationGenerator(applicationContext)

        notificationService.cancelNotification(1)
        snapshots = mutableListOf()

        val spare = intent?.getStringExtra("id").toString()
        if (spare != "null"&& spare.isNotBlank()) {
            id = spare

            prepareNotifications()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {

        snapshots.forEach {
            it.remove()
            notificationService.cancelNotification(snapshots.indexOf(it))
        }

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


    private fun prepareNotifications() {
        if (_notificationsChannel == null) db.collection("NotificationsChannels")
            .whereEqualTo("user", id).get().addOnSuccessListener { docs ->
                try {

                    _notificationsChannel = docs.documents[0].toObject(
                        NotificationChannel::class.java
                    )!!
                }catch (e:Exception){
                    prepareNotifications()
                    return@addOnSuccessListener
                }
                Log.i("Alo", _notificationsChannel!!.user)
                snapshots += db.collection("NotificationsChannels")
                    .document(_notificationsChannel!!.id).addSnapshotListener { value, error ->
                        if (value != null) {
                            val newNotif = value.toObject(
                                NotificationChannel::class.java
                            )!!

                            setLikesNotifications(newNotif)

                            val newValues =
                                newNotif.notifications - _notificationsChannel!!.notifications.toSet()
                                    .filter { !it.seen }.toSet()

                            _notificationsChannel = newNotif
                            for (i in newValues) {
                                when (i.type) {
                                    TypeOfNotification.Comment -> {
                                        newCommentNotification(i)
                                    }

                                    TypeOfNotification.Follow -> {
                                        getUser(i.sender) {
                                            notificationService.followingNotification(
                                                "Nuevo seguidor!",
                                                content = "${i.userName} te ha seguido!",
                                                url = it.profilePic
                                            )
                                        }

                                    }

                                    TypeOfNotification.NewPost -> {

                                        newPostNotification(i)
                                    }

                                    else -> {}
                                }
                                db.collection("NotificationsChannel").document(
                                    _notificationsChannel!!.id
                                ).update(
                                    "notifications.seen", true
                                )
                            }
                        }
                    }

            }
    }

    private fun getUser(sender: String, function: (Profile) -> Int) {
        db.collection("Users").document(sender).get().addOnSuccessListener {
            val user = it.toObject(Profile::class.java)!!
            function(user)
        }
    }

    private fun newCommentNotification(i: Notification) {
        val id = i.notificationText.split("=")[0]
        db.collection("Posts").document(id).get().addOnSuccessListener {

            val found = it.toObject<UIPost>()!!
            getSourceAndNotify(found){bitmap->

                notificationService.showNotSoBasicNotification(
                    "Nuevo comentario de ${i.userName}!",
                    content = i.notificationText.split("=")[1],
                    bitmap
                )
            }

        }

    }

    private fun newPostNotification(i: Notification) {

        val id = i.notificationText.split("=")[0]
        db.collection("Posts").document(id).get().addOnSuccessListener {

            val found = it.toObject<UIPost>()!!

            getSourceAndNotify(found){bitmap->
                notificationService.showNotSoBasicNotification(
                    "Nueva publicacion de ${i.userName}!",
                    content = "${i.userName}: ${i.notificationText.split("=")[1]}",
                    bitmap
                )
            }


        }

    }

    private fun setLikesNotifications(newNotif: NotificationChannel) {

        val newLikes =
            (newNotif.notifications - _notificationsChannel!!.notifications.toSet()).filter { it.type == TypeOfNotification.Like }

        val postsNotified = newLikes.map { it.notificationText }

        for (i in postsNotified){
            db.collection("Posts").document(i).get().addOnSuccessListener {
                val found = it.toObject<UIPost>()!!

                getSourceAndNotify(found){bitmap->
                    notificationService.showNotSoBasicNotification(
                        "A la gente le gusta tu post!",
                        content = "tienes ${newLikes.count { it.notificationText == i }} nuevos likes",
                        bitmap
                    )
                }
            }
        }

    }

    fun getSourceAndNotify(found:UIPost, notify :(Bitmap)->Unit){
        if (found.typeOfMedia == "image"){
            val imageLoader = ImageLoader(this)
            val request = ImageRequest.Builder(this).data(found.source)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            CoroutineScope(Dispatchers.Unconfined).launch {

                val result = (imageLoader.execute(request) as SuccessResult).drawable
                val bitmap = (result as BitmapDrawable).bitmap
                notify(bitmap)

            }
        }else{

            try {

                if (!File(
                        found.typeOfMedia + "s","mp4"
                    ).exists()
                ) {

                    val destination = File.createTempFile(
                        found.typeOfMedia + "s", "mp4"
                    )

                    storageRef.child("PostImages/${found.id}").getFile(destination)
                        .addOnSuccessListener {
                            found.UIURL = Uri.fromFile(destination)
                            found.mediaItem =
                                MediaItem.fromUri(found.UIURL)
                        }
                } else {
                    found.UIURL = Uri.fromFile(
                        File(
                            found.typeOfMedia + "s", "mp4"
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("tipo", e.stackTraceToString())
            }
            CoroutineScope(Dispatchers.Unconfined).launch {

                while (found.UIURL == Uri.EMPTY)
                    delay(100)
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(this@PetstagramNotificationsService, found.UIURL)
                val bitmap = retriever.getFrameAtIndex(1)!!

                notify(bitmap)
            }
        }

    }


}