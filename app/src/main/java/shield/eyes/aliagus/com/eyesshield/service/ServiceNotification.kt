package shield.eyes.aliagus.com.eyesshield.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import shield.eyes.aliagus.com.eyesshield.MainActivity
import shield.eyes.aliagus.com.eyesshield.R
import shield.eyes.aliagus.com.eyesshield.overlay.Filter
import shield.eyes.aliagus.com.eyesshield.overlay.Overlays

open class ServiceNotification : Service() {

    private var notificationCompat: Notification? = null
    private var startCODE = 1
    private lateinit var mFilter: Filter

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mFilter = Overlays(this)
    }

    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var preff = application.getSharedPreferences("preferencess", Context.MODE_PRIVATE)
        var isOn = preff.getBoolean("isOn", false)
        if (isOn) {
            createNotification()
        } else {
            destroyNotification()
        }
        return startCODE
    }

    private fun createNotification() {
        var intent = Intent(this@ServiceNotification, MainActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(this@ServiceNotification, 0, intent, 0)
        notificationCompat = NotificationCompat.Builder(this@ServiceNotification, "")
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Running")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_eye_black_24dp)
                .build()
        startForeground(1234, notificationCompat)
    }

    private fun destroyNotification() {
        if (notificationCompat != null) {
            stopForeground(true)
            notificationCompat = null
        }
    }
}