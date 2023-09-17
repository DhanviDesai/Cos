package com.dhanvi.cos

import android.app.Notification
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


private const val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
const val NOTIFICATION_LISTENER_INTENT = "com.dhanvi.cos.NOTIFICATION_SERVICE_EXAMPLE"
const val NOTIFICATION_EVENT = "notification_event"
const val COMMAND = "command"
const val LIST_COMMAND = "list"
const val IS_CONNECTED = "isConnected"

const val EXTRA_TITLE_KEY = "android.title"
const val EXTRA_TEXT_KEY = "android.text"
const val EXTRA_SUB_TEXT_KEY = "android.subText"
const val EXTRA_SUMMARY_TEXT_KEY = "android.summaryText"
const val EXTRA_TITLE_BIG_KEY = "android.title.big"
const val EXTRA_TEXT_LINES = "android.textLines"
const val EXTRA_BIG_TEXT = "android.bigText"

data class NotificationObject(
    @SerializedName("package_name")
    val packageName: String,
    @SerializedName("notification_title")
    val notificationTitle: String?,
    @SerializedName("notification_text")
    val notificationText: String?,
    @SerializedName("notification_title_big")
    val notificationTitleBig: String?,
    @SerializedName("notification_extra_sub_text")
    val notificationExtraSubText: String?,
    @SerializedName("notification_extra_summary_text")
    val notificationExtraSummaryText: String?,
    @SerializedName("notification_extra_text_lines")
    val notificationExtraTextLines: String?,
    @SerializedName("notification_text_big")
    val notificationTextBig: String?

)

class GsonInstance private constructor() {
    companion object {
        private var mInstance: Gson? = null
        fun getInstance(): Gson {
            if(mInstance == null) {
                mInstance = Gson()
            }
            return mInstance!!
        }
    }
}