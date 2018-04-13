package shield.eyes.aliagus.com.eyesshield

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        checkingPermission()
    }

    companion object {
        const val PERMISSION_CODE = 101
        val TAG = javaClass.simpleName
    }


    private fun checkingPermission() {
        var settingsCanWrite = Settings.System.canWrite(applicationContext)
        val granted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED
        var listPermission = arrayOf(android.Manifest.permission.WRITE_SETTINGS)
        /*if (!granted) {
            ActivityCompat.requestPermissions(this, listPermission, PERMISSION_CODE)
            return
        } else {
            setBrightness()
        }*/
        if (!settingsCanWrite) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + applicationContext.packageName)
            startActivity(intent)
            finish()
        } else {
            setBrightness()
        }
    }

    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                    //moveTaskToBack(true)
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                    setBrightness()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }*/

    private fun setBrightness() {
        seekBar_brightness.max = 225
        var curBrightnessValue = 0
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(contentResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

        var screenBrightness = curBrightnessValue
        seekBar_brightness.progress = screenBrightness
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}
