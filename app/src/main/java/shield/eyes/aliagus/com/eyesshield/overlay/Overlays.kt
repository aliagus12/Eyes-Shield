package shield.eyes.aliagus.com.eyesshield.overlay

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.WindowManager
import shield.eyes.aliagus.com.eyesshield.screen_manager.ScreenManager
import kotlin.properties.Delegates

/**
 * Created by ali on 17/04/18.
 */
class Overlays(context: Context): View(context), OrientationChangeReceiver.OnOrientationChangeListener, Filter {

    val TAG = javaClass.simpleName
    override fun onCreate() {
       Log.d(TAG, "onCreate")
        show()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        filtering = false
    }

    private val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val mScreenManager = ScreenManager(context, mWindowManager)
    private val mOrientationReceiver = OrientationChangeReceiver(context, this)
    //private val mBrightnessManager = BrightnessManager(context)

    override fun onOrientationChanged() {
        updateLayoutParams()
        reLayout()
    }

    /*override var profile = activeProfile.off
        set(value) {
            Log.i("profile set to: $value")
            field = value
            filtering = !value.isOff
        }*/

    private fun reLayout() = mWindowManager.updateViewLayout(this, mLayoutParams)

    private var mLayoutParams = mScreenManager.layoutParams
        get() = field.apply {
            //buttonBrightness = Config.buttonBacklightLevel
        }

    private fun updateLayoutParams() {
        mLayoutParams = mScreenManager.layoutParams
    }

    //override fun onDraw(canvas: Canvas) = canvas.drawColor(profile.filterColor)
    override fun onDraw(canvas: Canvas) = canvas.drawColor(context.getColor(android.R.color.holo_red_dark))

    private var filtering: Boolean by Delegates.observable(false) {
        _, isOn, turnOn -> when {
        !isOn && turnOn -> show()
        /*isOn && !turnOn -> hide()
        isOn && turnOn -> update()*/
    }
    }

    private fun show() {
        updateLayoutParams()
        mWindowManager.addView(this, mLayoutParams)
        mOrientationReceiver.register()
        //mBrightnessManager.brightnessLowered = profile.lowerBrightness
        //EventBus.register(this)
    }
}