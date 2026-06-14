package com.smartpocket.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.smartpocket.R
import com.smartpocket.ui.home.MainActivity

class SmartPocketService : Service() {

    companion object {
        const val CHANNEL_SERVICE = "sp_service"
        const val CHANNEL_ALERTS = "sp_alerts"
        const val NOTIF_SERVICE_ID = 1001
        const val NOTIF_ALERT_ID = 1002

        const val ACTION_DISARM = "com.smartpocket.ACTION_DISARM"
        const val ACTION_ALERT_BAG = "com.smartpocket.ALERT_BAG"
        const val ACTION_ALERT_BT = "com.smartpocket.ALERT_BT"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        startForeground(NOTIF_SERVICE_ID, buildServiceNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_DISARM -> {
                sendBroadcast(Intent("com.smartpocket.DISARM"))
            }
            ACTION_ALERT_BAG -> sendAlertNotification(
                title = getString(R.string.notif_bag_breach),
                body = "Bag sensor triggered. Tap to open Smart Pocket."
            )
            ACTION_ALERT_BT -> sendAlertNotification(
                title = getString(R.string.notif_bt_lost),
                body = "Guardian device moved out of range."
            )
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ─────────────────────────── NOTIFICATIONS ───────────────────────────

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Service channel (low priority — persistent)
            NotificationChannel(
                CHANNEL_SERVICE,
                getString(R.string.notif_channel_service),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Smart Pocket is running in the background"
                setShowBadge(false)
            }.also { nm.createNotificationChannel(it) }

            // Alert channel (high priority)
            NotificationChannel(
                CHANNEL_ALERTS,
                getString(R.string.notif_channel_security),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Security breach and proximity alerts"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 150, 300, 150, 600)
                enableLights(true)
                lightColor = 0xFFD95F5F.toInt()
            }.also { nm.createNotificationChannel(it) }
        }
    }

    private fun buildServiceNotification(): Notification {
        val openIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val disarmIntent = PendingIntent.getService(
            this, 1,
            Intent(this, SmartPocketService::class.java).apply { action = ACTION_DISARM },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_SERVICE)
            .setContentTitle(getString(R.string.notif_service_title))
            .setContentText("Bag sensor · Face ID · BT Guard")
            .setSmallIcon(R.drawable.ic_shield)
            .setContentIntent(openIntent)
            .setOngoing(true)
            .setSilent(true)
            .addAction(R.drawable.ic_lock, "Disarm", disarmIntent)
            .setColor(0xFF4A7FBD.toInt())
            .build()
    }

    fun sendAlertNotification(title: String, body: String) {
        val openIntent = PendingIntent.getActivity(
            this, 2,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ALERTS)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_shield)
            .setContentIntent(openIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 300, 150, 300, 150, 600))
            .setColor(0xFFD95F5F.toInt())
            .build()

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIF_ALERT_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Restart via BootReceiver if killed
    }
}
