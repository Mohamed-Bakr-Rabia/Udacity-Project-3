package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var title:String
    private var state:Int = 0
    private lateinit var downloadManager:DownloadManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadManager =  getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        notificationManager = ContextCompat.getSystemService(applicationContext,NotificationManager::class.java) as NotificationManager

        custom_button.setOnClickListener {
            when(radio_group.checkedRadioButtonId){
                R.id.radioButton -> download(glideURL,resources.getString(R.string.radio_button_1))
                R.id.radioButton2 -> download(udacityURL,resources.getString(R.string.radio_button_2))
                R.id.radioButton3-> download(retrofitURL,resources.getString(R.string.radio_button_3))
                else -> Toast.makeText(applicationContext,"Please select the file to download ",Toast.LENGTH_SHORT).show()
            }
            (it as LoadingButton).playAnimation()

        }

    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("Range")
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)



            if (id == downloadID){

                val query = DownloadManager.Query()
                query.setFilterById(downloadID)

                val cursor = downloadManager.query(query)
                cursor.moveToFirst()
                state = try {
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                } catch (e:Exception){
                    DownloadManager.STATUS_FAILED
                }

                createChannel("Download")
                when(state){
                    DownloadManager.STATUS_SUCCESSFUL -> notificationManager.sendNotification(title,applicationContext,CHANNEL_ID,"Successful")
                    DownloadManager.STATUS_FAILED -> notificationManager.sendNotification(title,applicationContext,CHANNEL_ID,"Fail")
                }

            }
        }
    }

    private fun download(URL: String, name:String) {
        title = name
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.


    }

    companion object {
        private const val udacityURL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"

        private const val glideURL =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"

        private const val retrofitURL =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"

        private const val CHANNEL_ID = "channelId"
    }


    fun createChannel(channelName: String){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val notificationChannel = NotificationChannel(CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_LOW)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = applicationContext.getString(R.string.notification_description)
            notificationManager.createNotificationChannel(notificationChannel)
        }


    }
}
