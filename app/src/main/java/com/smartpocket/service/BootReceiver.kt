package com.smartpocket.service

import android.app.admin.DeviceAdminReceiver
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action in listOf(
                Intent.ACTION_BOOT_COMPLETED,
                "android.intent.action.QUICKBOOT_POWERON"
            )) {
            // Restart the security service after device reboot
            ContextCompat.startForegroundService(
                context,
                Intent(context, SmartPocketService::class.java)
            )
        }
    }
}

class SmartPocketDeviceAdmin : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        // Device admin enabled — lockdown mode fully available
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        // Device admin removed — lockdown degraded (screen lock won't work)
    }

    companion object {
        fun getComponentName(context: Context): ComponentName {
            return ComponentName(context, SmartPocketDeviceAdmin::class.java)
        }
    }
}
