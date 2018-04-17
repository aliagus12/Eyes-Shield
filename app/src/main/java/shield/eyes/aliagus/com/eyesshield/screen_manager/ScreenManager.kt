package shield.eyes.aliagus.com.eyesshield.screen_manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.PixelFormat
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.WindowManager

/**
 * Created by ali on 17/04/18.
 */

private const val DEFAULT_NAV_BAR_HEIGHT_DP = 48
private const val DEFAULT_STATUS_BAR_HEIGHT_DP = 25

class ScreenManager(context: Context, private val mWindowManager: WindowManager) {

    private val mResources: Resources = context.resources
    private var mStatusBarHeight = -1
    private var mNavigationBarHeight = -1

    val layoutParams: WindowManager.LayoutParams
        get() = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                screenHeight,
                0,
                -statusBarHeightPx,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                        or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }

    val screenHeight: Int
        @SuppressLint("NewApi")
        get() {
            val display = mWindowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getRealMetrics(displayMetrics)
            return if (inPortrait) {
                displayMetrics.heightPixels + statusBarHeightPx + navigationBarHeightPx
            } else {
                displayMetrics.heightPixels + statusBarHeightPx
            }
        }

    val statusBarHeightPx: Int
        get() {
            if (mStatusBarHeight == -1) {
                val statusBarHeightId = mResources.getIdentifier("status_bar_height", "dimen", "android")

                if (statusBarHeightId > 0) {
                    mStatusBarHeight = mResources.getDimensionPixelSize(statusBarHeightId)
                    Log.i("Found Status Bar Height: " + mStatusBarHeight)
                } else {
                    mStatusBarHeight = dpToPx(DEFAULT_STATUS_BAR_HEIGHT_DP)
                    Log.i("Using default Status Bar Height: " + mStatusBarHeight)
                }
            }

            return mStatusBarHeight
        }

    val navigationBarHeightPx: Int
        get() {
            if (mNavigationBarHeight == -1) {
                val navBarHeightId = mResources.getIdentifier("navigation_bar_height", "dimen", "android")

                if (navBarHeightId > 0) {
                    mNavigationBarHeight = mResources.getDimensionPixelSize(navBarHeightId)
                    Log.i("Found Navigation Bar Height: " + mNavigationBarHeight)
                } else {
                    mNavigationBarHeight = dpToPx(DEFAULT_NAV_BAR_HEIGHT_DP)
                    Log.i("Using default Navigation Bar Height: " + mNavigationBarHeight)
                }
            }

            return mNavigationBarHeight
        }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(), mResources.displayMetrics).toInt()
    }

    private val inPortrait: Boolean
        get() = mResources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}