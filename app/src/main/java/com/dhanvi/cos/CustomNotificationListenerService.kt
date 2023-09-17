package com.dhanvi.cos

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson

private const val TAG = "CustomNotificationListenerService"

class CustomNotificationListenerService : NotificationListenerService() {

    private var customNotificationListenerReceiver: CustomNotificationListenerReceiver? = null

    // Cannot override onBind, maybe android internally uses it to call the methods
    // Communication has to be via broadcasts
    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() called")
        customNotificationListenerReceiver = CustomNotificationListenerReceiver()
        val filter = IntentFilter(NOTIFICATION_LISTENER_INTENT)
        registerReceiver(customNotificationListenerReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(customNotificationListenerReceiver)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "onListenerConnected() called")
        val intent = Intent(NOTIFICATION_LISTENER_INTENT)
        intent.putExtra(IS_CONNECTED, true)
        sendBroadcast(intent)
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d(TAG, "onListenerDisconnected() called")
        val intent = Intent(NOTIFICATION_LISTENER_INTENT)
        intent.putExtra(IS_CONNECTED, false)
        sendBroadcast(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {

    }

    inner class CustomNotificationListenerReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if(intent.getStringExtra(COMMAND).equals(LIST_COMMAND)) {
                    activeNotifications.forEach { sbn ->
                        Log.d(TAG, "onReceive() called with: sbn = ${sbn.notification.extras}")
                        val i = Intent(NOTIFICATION_LISTENER_INTENT)
                        val notificationObject = NotificationObject(sbn.packageName,
                            sbn.notification.extras.getString(EXTRA_TITLE_KEY),
                            sbn.notification.extras.getString(EXTRA_TEXT_KEY),
                            sbn.notification.extras.getString(EXTRA_TITLE_BIG_KEY),
                            sbn.notification.extras.getString(EXTRA_SUB_TEXT_KEY),
                            sbn.notification.extras.getString(EXTRA_SUMMARY_TEXT_KEY),
                            sbn.notification.extras.getString(EXTRA_TEXT_LINES),
                            sbn.notification.extras.getString(EXTRA_BIG_TEXT))
                        if(sbn.packageName.equals("com.google.android.gm")) {
                            Log.d(TAG, "onReceive() called with: sbn = ${sbn.notification.extras}")
                        }
                        i.putExtra(NOTIFICATION_EVENT, GsonInstance.getInstance().toJson(notificationObject).toString())
                        sendBroadcast(i)
                    }
                }
            }
        }

    }
}