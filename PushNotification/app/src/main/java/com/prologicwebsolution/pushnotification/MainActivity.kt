package com.prologicwebsolution.pushnotification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RemoteViews
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import java.lang.Exception
import java.nio.channels.Channel
import java.util.jar.Attributes

class MainActivity : AppCompatActivity() {

    private var channel_id = "Channel_id"
     var title:TextView? = null
     var msg:TextView? = null
     var image: ImageView? =  null


    @SuppressLint("RemoteViewLayout")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = findViewById(R.id.titleTxt)
        msg = findViewById(R.id.descrit)
        image = findViewById(R.id.img)

        val intent = intent
        if(intent != null){
            try {
            val message = intent.getStringExtra("message")
            val titleDat = intent.getStringExtra("title")
            val imageUrl = intent.getStringExtra("imageUrl")

            title!!.text = titleDat
            msg!!.text = message


                Glide
                    .with(this)
                    .load(imageUrl)
                    .centerCrop()
//                    .placeholder(R.drawable.loading_spinner)
                    .into(image!!);
            }catch (e: Exception){
                e.printStackTrace()
            }
        }




//        createNotification()
//        val notificationlayout = RemoteViews(packageName,R.layout.custom_notification_items)
//        var builder: NotificationCompat = NotificationCompat.Builder(this,channel_id).setContentTitle("")
    }

   /* fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "App Notification"
            val desTxt = "This is Your notification Description"
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channel_id, name, importance).apply {
                description = desTxt
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }*/

}