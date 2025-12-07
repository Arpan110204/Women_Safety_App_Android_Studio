package com.example.womansafetyapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AlertActivity : AppCompatActivity() {

    // Change to your real emergency number
    private val emergencyNumber = "+91XXXXXXXXXX"

    private var countDownTimer: CountDownTimer? = null
    private var sosAlreadySent = false   // prevent multiple sends

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnSendSOS = findViewById<Button>(R.id.btnSendSOS)
        val tvTimer = findViewById<TextView>(R.id.tvTimer)

        // Start 10-second countdown
        startCountdown(tvTimer)

        // User says they are safe → stop everything
        btnCancel.setOnClickListener {
            cancelCountdown()

            // Re-arm motion detection in service
            val serviceIntent = Intent(this, MotionService::class.java)
            serviceIntent.putExtra("RESET", true)
            ContextCompat.startForegroundService(this, serviceIntent)

            Toast.makeText(this, "Alert cancelled. You are marked safe.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // User taps Send SOS manually
        btnSendSOS.setOnClickListener {
            cancelCountdown()
            sendEmergencySOS()
        }
    }

    private fun startCountdown(tvTimer: TextView) {
        countDownTimer = object : CountDownTimer(10_000, 1_000) {  // 10 seconds
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                tvTimer.text = "Auto SOS in ${secondsLeft}s"
            }

            override fun onFinish() {
                if (!sosAlreadySent) {
                    sendEmergencySOS()
                }
            }
        }.start()
    }

    private fun cancelCountdown() {
        countDownTimer?.cancel()
        countDownTimer = null
    }

    private fun sendEmergencySOS() {
        if (sosAlreadySent) return
        sosAlreadySent = true

        val location = getLastKnownLocation()

        val message = if (location != null) {
            """
            🚨 EMERGENCY ALERT 🚨
            I may be in danger. Please call or reach me immediately.

            📍 My approximate location:
            https://maps.google.com/?q=${location.latitude},${location.longitude}

            📱 Sent from Women Safety App.
            """.trimIndent()
        } else {
            """
            🚨 EMERGENCY ALERT 🚨
            I may be in danger. Please call or reach me immediately.

            ⚠️ Location not available (GPS off or no signal).
            📱 Sent from Women Safety App.
            """.trimIndent()
        }

        // 1️⃣ Try to auto-send SMS
        val sent = sendSMSDirect(emergencyNumber, message)

        // 2️⃣ Always open SMS app as backup
        openSmsApp(emergencyNumber, message)

        if (sent) {
            Toast.makeText(
                this,
                "Auto SOS requested. SMS app also opened as backup.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this,
                "Could not auto-send. Please press SEND in SMS app.",
                Toast.LENGTH_LONG
            ).show()
        }

        // 3️⃣ Start emergency call
        startEmergencyCall()

        // 4️⃣ Re-arm motion detection after SOS flow
        val serviceIntent = Intent(this, MotionService::class.java)
        serviceIntent.putExtra("RESET", true)
        ContextCompat.startForegroundService(this, serviceIntent)

        // Close alert screen
        finish()
    }

    /** Try to send SMS directly using SmsManager. Returns true if command sent to system. */
    private fun sendSMSDirect(number: String, message: String): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        return try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number, null, message, null, null)
            true
        } catch (e: Exception) {
            false
        }
    }

    /** Open default SMS app with message pre-filled as backup. */
    private fun openSmsApp(number: String, message: String) {
        try {
            val uri = Uri.parse("smsto:$number")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", message)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No SMS app found to send SOS.", Toast.LENGTH_LONG).show()
        }
    }

    /** Get last known location from available providers. */
    private fun getLastKnownLocation(): Location? {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)

        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return null
            }

            val location = locationManager.getLastKnownLocation(provider)
            if (location != null) return location
        }
        return null
    }

    /** Start an emergency call to the same emergency number. */
    private fun startEmergencyCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$emergencyNumber")
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(callIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Call failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelCountdown()
    }
}
