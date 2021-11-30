package com.csestateconnect.utils

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.csestateconnect.R
import com.csestateconnect.ui.navdrawer.calenderEvents.NotificationMessageActivity


class AlarmBroadcast: BroadcastReceiver() {

    private val NOTIFICATION_ID = 42
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        val subject = bundle.getString("subject")
        val descript = bundle.getString("descript")
        val priority = bundle.getString("priority")
        val category = bundle.getString("category")
        val date = bundle.getString("date") + " " + bundle.getString("time")

        //Click on Notification
        val intent1 = Intent(context, NotificationMessageActivity::class.java)
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent1.putExtra("subject", subject)
        intent1.putExtra("descript", descript)
        intent1.putExtra("priority", priority)
        intent1.putExtra("category", category)
        //Notification Builder

        val pendingIntent: PendingIntent = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(intent1)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

//        val pendingIntent =
//            PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder =
            NotificationCompat.Builder(context, "notify_001")
        val contentView =
            RemoteViews(context.packageName, R.layout.notification_layout)
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher)
        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//        val pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
//        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent)
        contentView.setTextViewText(R.id.message, subject)
        contentView.setTextViewText(R.id.date, date)
        mBuilder.setSmallIcon(R.drawable.ic_alarm_white_24dp)
        mBuilder.setAutoCancel(true)
        mBuilder.setOngoing(false)
        mBuilder.priority = Notification.PRIORITY_HIGH
        mBuilder.setOnlyAlertOnce(true)
        mBuilder.setSound(soundUri)
        mBuilder.build().flags =
            Notification.FLAG_NO_CLEAR or Notification.PRIORITY_HIGH
        mBuilder.setContent(contentView)
        mBuilder.setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id"
            val channel =
                NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)

        }
        val notification = mBuilder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
