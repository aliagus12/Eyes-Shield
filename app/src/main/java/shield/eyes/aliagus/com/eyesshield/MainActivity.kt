package shield.eyes.aliagus.com.eyesshield

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import shield.eyes.aliagus.com.eyesshield.service.ServiceNotification

class MainActivity : AppCompatActivity() {

    lateinit var preff: SharedPreferences
    var isGranted = false
    val TAG = javaClass.simpleName

    companion object {
        const val PERMISSION_CODE_OVERLAYS = 102
        const val PERMISSION_CODE_BRIGHTNESS = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar_brightness.max = 225
        seekBar_brightness.isEnabled = false
        preff = getSharedPreferences("preferencess", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        checkingPermissionOverlay()
    }

    private fun checkingPermissionOverlay() {
        var grantedOverlay = Settings.canDrawOverlays(applicationContext)
        if (!grantedOverlay) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:" + applicationContext.packageName)
            startActivityForResult(intent, PERMISSION_CODE_OVERLAYS)
        } else {
            setComponent()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var settingsCanWrite = Settings.System.canWrite(applicationContext)
        var isGrantedOVerlays = Settings.canDrawOverlays(applicationContext)
        when (requestCode) {
            PERMISSION_CODE_OVERLAYS -> {
                if (!isGrantedOVerlays) {
                    onBackPressed()
                } else {
                    setComponent()
                }
            }

            PERMISSION_CODE_BRIGHTNESS -> {
                if (settingsCanWrite) {
                    setSeekbar(isGranted)
                } else {
                    onBackPressed()
                }
            }
        }
    }

    private fun setComponent() {
        var isOn = preff.getBoolean("isOn", false)
        var isAllowBrightness = preff.getBoolean("isAllow", false)
        Log.d(TAG, "component $isAllowBrightness")
        var curBrightnessValue = 0
        curBrightnessValue = android.provider.Settings.System.getInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS)
        var screenBrightness = curBrightnessValue
        seekBar_brightness.progress = screenBrightness
        valueSeekbar.text = screenBrightness.toString()
        switch_on_off.isChecked = isOn
        switch_allow_brightness.isChecked = isAllowBrightness
        switch_allow_brightness.isEnabled = isOn
        seekBar_brightness.isEnabled = isOn && isAllowBrightness

        switch_on_off.setOnCheckedChangeListener { _, isOn ->
            switch_on_off.isChecked = isOn
            switch_allow_brightness.isEnabled = isOn
            seekBar_brightness.isEnabled = isOn && switch_allow_brightness.isChecked
            saveChangeListener(isOn, "on-off")
            startService(Intent(this@MainActivity, ServiceNotification::class.java))
        }

        setBrightness()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setBrightness() {
        switch_allow_brightness.setOnCheckedChangeListener { _, isAllow ->
            isGranted = isAllow
            checkingPermissionBrightness(isAllow)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkingPermissionBrightness(isAllow: Boolean) {
        var settingsCanWrite = Settings.System.canWrite(applicationContext)
        if (!settingsCanWrite) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + applicationContext.packageName)
            startActivityForResult(intent, PERMISSION_CODE_BRIGHTNESS)
        } else {
            setSeekbar(isAllow)
        }
    }

    private fun setSeekbar(isAllow: Boolean) {
        seekBar_brightness.isEnabled = isAllow
            saveChangeListener(isAllow, "brightness")
            try {
                seekBar_brightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    var progress = 0
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        progress = p1
                        valueSeekbar.text = progress.toString()
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                        android.provider.Settings.System.putInt(
                                contentResolver,
                                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                                progress
                        )
                    }
                })
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }
    }

    private fun saveChangeListener(boolean: Boolean, string: String) {
        var editor = preff.edit()
        if (string == "brightness") {
            editor.putBoolean("isAllow", boolean)
        } else if (string == "on-off") {
            editor.putBoolean("isOn", boolean)
        }
        editor.apply()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        finish()
    }

    fun onButtonRedClicked(view: View) {

    }

    fun onButtonYellowClicked(view: View){

    }

    fun onButtonBlueClicked(view: View){

    }
}
