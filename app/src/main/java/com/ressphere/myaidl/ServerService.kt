package com.ressphere.myaidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ServerService : Service() {

    private val myService = MyService()

    override fun onBind(intent: Intent): IBinder {
        return myService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return START_STICKY;
    }

    private fun start() {
        val notification = NotificationCompat
            .Builder(this, "running_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("Server Service")
            .build()
        startForeground(1, notification)
    }

    enum class Actions {
        START,
        STOP
    }
}