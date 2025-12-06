🚨 Women Safety App – Android Application

A smart real-time personal safety app using motion detection, SOS alerts, automatic calls, and location sharing.

📌 Overview

The Women Safety App is an Android-based emergency response system that automatically detects unusual or violent phone motion using accelerometer data.
When danger is detected, it triggers an alert screen with a countdown — if the user doesn’t respond, the app sends:

📍 Live location SOS via SMS

📞 Automatic emergency call

🆘 Pre-filled SMS message as backup

The app runs as a foreground service, ensuring protection stays active even when running in background.

✨ Features (Current Build)
🛡 Motion Detection

Detects violent shaking or sudden movements

Uses accelerometer in real time

Intelligent single-trigger system (no repeated false alerts)

🚨 Alert Mode

10-second countdown timer

“I’m Safe” cancel button

“Send SOS Now” manual trigger

🆘 SOS Response

Sends SMS with Google Maps location link

Opens SMS app with a pre-filled emergency message

Automatically calls the emergency contact

🔔 Foreground Service

Protection stays ON even when the app is in background

Notification: “Protection Active – Monitoring motion for your safety”

🎨 Modern UI

Animated circular protection button
Glow effects
Clean alert screen with red gradient
Separate screens for dashboard + alert

🧠 Tech Stack
Languages

Kotlin (Primary)
Android Components
Foreground Service
SensorManager (Accelerometer)
LocationManager
CountDownTimer
Intent-based SMS & CALL APIs
Notification Channels
Runtime Permissions
UI / UX
XML Layouts
Custom Drawables
Animations (pulse effect)
Gradient backgrounds

📱 App Screenshots (To Add)

Add images here later

/assets/home_screen.png  
/assets/alert_screen.png  

```
🛠 Architecture Overview

MainActivity
│
├── Controls protection ON/OFF
├── Requests permissions
└── Starts/stops MotionService


MotionService (Foreground Service)
│
├── Runs accelerometer listener
├── Detects sudden motion
└── Launches AlertActivity


AlertActivity
│
├── 10-sec countdown
├── Cancel button → Safe
├── Auto SOS → SMS + Call
└── Re-arms MotionService 




```

📍 Permissions Used
android.permission.SEND_SMS  
android.permission.CALL_PHONE  
android.permission.ACCESS_FINE_LOCATION  
android.permission.ACCESS_COARSE_LOCATION  
android.permission.FOREGROUND_SERVICE  
android.permission.FOREGROUND_SERVICE_LOCATION  
android.permission.POST_NOTIFICATIONS


These permissions allow:

Sending emergency messages
Making calls
Getting location
Running protection in background

▶️ How to Run
1. Clone the repository
git clone https://github.com/<your-username>/Women_Safety_App_Android_Studio.git

2. Open in Android Studio

Android Studio → Open → Select project folder

3. Build the project

Build > Rebuild Project

4. Run on Android device

Enable Developer Options

Enable USB debugging OR Wireless debugging

Press ▶️ Run button

5. Allow permissions

App will ask for:

SMS
Phone
Location
Notifications
All must be granted.

🔬 Testing Motion Detection

Open app
Tap START → Protection ON
Press Home
Shake phone strongly
Alert screen should appear
Let countdown finish → SOS triggers
```
🚀 Future Enhancements
WhatsApp SOS
Pre-filled WhatsApp message
One-tap emergency message
Twilio Cloud SMS
Send SOS even without SIM
Works internationally
Bracelet / IoT Integration
Trigger SOS using BLE wearable
Real-time Tracking
Send periodic GPS updates to guardian
Voice Activation
Trigger SOS on keyword: “Help me”
Audio/Video Evidence
Auto-record during alert
Upload to cloud
```
👨‍💻 Developer
```
Arpan Mukherjee
B.Tech CSE, Academy of Technology
Passionate about building useful real-world systems.
