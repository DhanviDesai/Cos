package com.dhanvi.cos

import android.app.AlertDialog
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.dhanvi.cos.ui.theme.CosTheme
import com.google.gson.Gson

private const val TAG = "MainActivity"


class MainActivity : ComponentActivity() {

    private lateinit var mService: CustomNotificationListenerService
    private var mBound: Boolean = false

    private var mainNotificationReceiver: MainNotificationReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()

        if(!NotificationManagerCompat.getEnabledListenerPackages(this).contains(packageName)) {
            buildNotificationServiceAlertDialog().show()
        }

        mainNotificationReceiver = MainNotificationReceiver()
        val filter = IntentFilter(NOTIFICATION_LISTENER_INTENT)
        registerReceiver(mainNotificationReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mainNotificationReceiver)
    }

    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission please")
        builder.setMessage("Please give notifications permission")
        builder.setPositiveButton("yes"
        ) { dialog, which -> startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        builder.setNegativeButton("no") { dialog, which ->

        }
        return builder.create()
    }

    inner class MainNotificationReceiver: BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if(it.getBooleanExtra(IS_CONNECTED, false)) {
                    Log.d(TAG, "onReceive() called and is connected")
                    val i = Intent(NOTIFICATION_LISTENER_INTENT)
                    i.putExtra("command", "list")
                    sendBroadcast(i)
                }
                if(!it.getStringExtra(NOTIFICATION_EVENT).isNullOrEmpty()) {
                    val notString = it.getStringExtra(NOTIFICATION_EVENT)
                    Log.d(TAG, "onReceive() called ${notString}")
                    val n = GsonInstance.getInstance().fromJson(notString.toString(), NotificationObject::class.java)
                }
            }
        }

    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun NotificationRow(title: String, text: String) {
    Row {
       Column {
           Text(
               text = title
           )
           Text(
               text = text
           )
       }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CosTheme {
        Greeting("Android")
    }
}