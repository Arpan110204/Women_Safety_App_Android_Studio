package com.example.womansafetyapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val permissionRequestCode = 1001
    private var protectionOn = false   // track ON/OFF state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnProtection = findViewById<LinearLayout>(R.id.btnProtection)
        val imgGlow = findViewById<ImageView>(R.id.imgGlow)
        val tvProtectionText = findViewById<TextView>(R.id.tvProtectionText)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        requestAllPermissions()

        btnProtection.setOnClickListener {

            if (!protectionOn) {
                // Turning ON
                protectionOn = true
                startMotionService()

                tvProtectionText.text = "STOP"
                tvStatus.text = "Protection is ON"

                // Glow effect
                imgGlow.visibility = View.VISIBLE
                imgGlow.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.pulse)
                )

                // Change button to green active state
                btnProtection.setBackgroundResource(R.drawable.protection_button_on)

            } else {
                // Turning OFF
                protectionOn = false
                stopMotionService()

                tvProtectionText.text = "START"
                tvStatus.text = "Protection is OFF"

                // Stop glow
                imgGlow.clearAnimation()
                imgGlow.visibility = View.GONE

                // Change button back to red
                btnProtection.setBackgroundResource(R.drawable.protection_button)
            }
        }
    }

    private fun startMotionService() {
        val intent = Intent(this, MotionService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    private fun stopMotionService() {
        val intent = Intent(this, MotionService::class.java)
        stopService(intent)
    }

    private fun requestAllPermissions() {
        val permissions = arrayOf(
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )

        val needed = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (needed.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                needed.toTypedArray(),
                permissionRequestCode
            )
        }
    }

}
