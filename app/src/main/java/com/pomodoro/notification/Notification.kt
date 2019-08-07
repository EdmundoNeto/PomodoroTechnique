package com.pomodoro.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

import androidx.core.app.NotificationCompat

import com.pomodoro.MainActivity
import com.pomodoro.R

class Notification(private val context: Context) {

    fun notifyUser() {

        val builder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(context!!.getString(R.string.app_name))
                .setContentText(context.getString(R.string.pomodoro_finished))
                .setContentIntent(pendingIntent())
                .setVibrate(longArrayOf(500, 500, 500, 500, 500))
        val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel_name)
            val descriptionText = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(context.getString(R.string.notification_channel_id), name, importance)
            mChannel.description = descriptionText
            mNotifyMgr.createNotificationChannel(mChannel)
        }

        mNotifyMgr.notify(1, builder.build())
    }

    private fun pendingIntent(): PendingIntent {
        val resultIntent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}