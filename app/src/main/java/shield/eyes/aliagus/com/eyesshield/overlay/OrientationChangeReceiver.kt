package shield.eyes.aliagus.com.eyesshield.overlay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class OrientationChangeReceiver(
        private val mContext: Context,
        private val mListener: OnOrientationChangeListener)
        : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_CONFIGURATION_CHANGED) {
            mListener.onOrientationChanged()
        }
    }

    private val filter by lazy {
        IntentFilter().apply {
            addAction(Intent.ACTION_CONFIGURATION_CHANGED)
        }
    }

    fun register(): Intent? = mContext.registerReceiver(this, filter)

    fun unregister() = mContext.unregisterReceiver(this)

    interface OnOrientationChangeListener {
        fun onOrientationChanged()
    }
}
