package com.udacity.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R


private const val NOTIFICATION_ID = 0

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(messageBody:String, applicationContext: Context, channelId:String, state:String){

    val intent = Intent(applicationContext,DetailActivity::class.java)

    intent.putExtra("FileName",messageBody)
    intent.putExtra("state",state)

    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(applicationContext,channelId)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setStyle(NotificationCompat.BigTextStyle())
        .addAction(R.drawable.ic_baseline_cloud_download_24,"Check the status",pendingIntent)


    notify(NOTIFICATION_ID,builder.build())

}


fun NotificationManager.cancelNotification(){
    cancelAll()
}