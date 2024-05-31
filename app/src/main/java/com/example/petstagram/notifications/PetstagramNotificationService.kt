package com.example.petstagram.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.example.petstagram.Petstagram
import com.example.petstagram.R
import kotlin.random.Random

class PetstagramNotificationService(
    private val context: Context
){
    private val notificationManager=context.getSystemService(NotificationManager::class.java)
    private var counter = 0
    fun showBasicNotification(title : String = "", content : String = ""):Int{
        if (!hasPermission){
            Toast.makeText(context,content, Toast.LENGTH_SHORT).show()
            return -1
        }

        val resultIntent = Intent(context, Petstagram::class.java)
// Create the TaskStackBuilder.
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack.
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack.
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        val notification= NotificationCompat.Builder(context,"petstagram_notifications")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .build()

        notificationManager.notify(
            counter,
            notification
        )
        counter++
        return counter
    }

    fun cancelNotification(id:Int){
        notificationManager.cancel(id)
    }

    private fun Context.bitmapFromResource(
        @DrawableRes resId:Int
    )= BitmapFactory.decodeResource(
        resources,
        resId
    )

    companion object{
        var hasPermission = false
    }
}