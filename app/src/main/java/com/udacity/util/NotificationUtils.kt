package com.udacity.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.udacity.MainActivity
import com.udacity.R


private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody:String,applicationContext: Context){

    val intent = Intent(applicationContext,MainActivity::class.java)

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat
        .Builder(
            applicationContext,
            applicationContext.getString(R.string.notification_channel_id)
        )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)


    notify(NOTIFICATION_ID,builder.build())

}


fun createChannel(channelName: String,applicationContext: Context){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    {
        val notificationChannel = NotificationChannel(applicationContext.getString(R.string.notification_channel_id),channelName,NotificationManager.IMPORTANCE_LOW)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = applicationContext.getString(R.string.notification_description)

    }

}

fun NotificationManager.cancelNotification(){
    cancelAll()
}