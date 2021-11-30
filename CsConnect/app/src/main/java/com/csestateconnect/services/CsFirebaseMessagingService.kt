package com.csestateconnect.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.csestateconnect.R
import com.csestateconnect.db.MyDatabase
import com.csestateconnect.network.AuthApi
import com.csestateconnect.ui.home.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class CsFirebaseMessagingService : FirebaseMessagingService() {

    val api = AuthApi()
    val database = MyDatabase.getInstance(this)

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

//        devices[i].send_message(title=title, body=body, data={"id": record_id, "click_action": click_action})

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")

//            val title = remoteMessage.notification!!.title
//            val description = remoteMessage.notification!!.body
//            val id = remoteMessage.data.get("id")
//            val action = remoteMessage.data.get("click_action")
//
//            if (id != null) {
//                val response = NotificationData(id.toInt(), action!!,  title!!, description!!)
//                database.notificationDao.insert(response)
//            }
//            else {
//                val response = NotificationData(0, action!!, title!!, description!!)
//                database.notificationDao.insert(response)
//            }

            sendNotification(it, remoteMessage.data)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)

        val PRIVATE_MODE = 0
        val PREF_NAME = "regToken"
        val sharedPrefer: SharedPreferences = application.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        val editor = sharedPrefer.edit()
        editor.putString(PREF_NAME, token)
        editor.apply()
    }
    // [END on_new_token]


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(
        remoteMessage: RemoteMessage.Notification,
        data: MutableMap<String, String>
    ) {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val id = data.get("id") as Bundle
//        val bundle = bundleOf("notfyId" to data.get("id"))
        val action = data.get("click_action")
        Log.d("tagACTION", action)

        val title = remoteMessage.title
        val messageBody = remoteMessage.body

        val channelId: String?
        if (remoteMessage.channelId.isNullOrEmpty()){
            channelId = "fcm_default_channel_id"
        }
        else {
            channelId = remoteMessage.channelId
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        lateinit var notificationBuilder: NotificationCompat.Builder

        when (action){
            "LEAD_CREATE" -> {
                val bundle = bundleOf("notifyId" to data.get("id"))

                val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(HomeActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.leadDetailsFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

                notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                    .setSmallIcon(R.drawable.ic_logo_csconnect_orange)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            }

            "DEAL_CREATE" -> {
                val bundle = bundleOf("notifyId" to data.get("id"))

                val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(HomeActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.dealDetailsFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

                notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                    .setSmallIcon(R.drawable.ic_logo_csconnect_orange)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            }

            "LEADREQUEST_UPDATE" -> {
                val bundle = bundleOf("notifyId" to data.get("id"))

                val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(HomeActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.navigation_assigned_leads)
                    .setArguments(bundle)
                    .createPendingIntent()

                notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                    .setSmallIcon(R.drawable.ic_logo_csconnect_orange)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            }

            "CONCERN_UPDATE" -> {
                val bundle = bundleOf("notifyId" to data.get("id"))

                val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(HomeActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.concernTicketDetailsFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

                notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                    .setSmallIcon(R.drawable.ic_logo_csconnect_orange)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            }

            "CONCERNCOMMENT_CREATE" -> {
                val bundle = bundleOf("notifyId" to data.get("id"))

                val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(HomeActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.concernTicketDetailsFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

                notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                    .setSmallIcon(R.drawable.ic_logo_csconnect_orange)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            }

            "USER_UPDATE" -> {
                val bundle = bundleOf("notifyId" to data.get("id"))

                val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(HomeActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.nav_get_touch)
                    .setArguments(bundle)
                    .createPendingIntent()

                notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                    .setSmallIcon(R.drawable.ic_logo_csconnect_orange)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            }

            "BANKACCOUNT_UPDATE" -> {
                val bundle = bundleOf("notifyId" to data.get("id"))

                val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(HomeActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.header_view)
                    .setArguments(bundle)
                    .createPendingIntent()

                notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                    .setSmallIcon(R.drawable.ic_logo_csconnect_orange)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            }

            "KYC_UPDATE" -> {
                val bundle = bundleOf("notifyId" to data.get("id"))

                val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(HomeActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.header_view)
                    .setArguments(bundle)
                    .createPendingIntent()

                notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                    .setSmallIcon(R.drawable.ic_logo_csconnect_orange)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            }

            "COMMISSIONSLAB_CREATE" -> {
                val bundle = bundleOf("notifyId" to data.get("id"))

                val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(HomeActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.projectDetailFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

                notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                    .setSmallIcon(R.drawable.ic_logo_csconnect_orange)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            }

            else -> {
                val bundle = bundleOf("notifyId" to data.get("id"))

                val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(HomeActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.navigation_home)
                    .setArguments(bundle)
                    .createPendingIntent()

                notificationBuilder = NotificationCompat.Builder(this, channelId!!)
                    .setSmallIcon(R.drawable.ic_logo_csconnect_orange)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
            }
        }

//        val intent = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }

//        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, 0)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Important",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

    }

    companion object {

        private const val TAG = "CsFirebaseMsgService"
    }
}