package com.justtype.shellkeyboard.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Privacy policy activity.
 * 
 * Shell Keyboard is privacy-first:
 * - No internet permission required for normal use
 * - No data leaves the device unless LAN sync is enabled
 * - Password fields are never logged
 * - All input processing happens locally via RIME
 */
class PrivacyPolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val textView = TextView(this).apply {
            text = PRIVACY_POLICY
            setPadding(32, 32, 32, 32)
            textSize = 14f
        }
        
        setContentView(textView)
        title = "Privacy Policy"
    }

    companion object {
        const val PRIVACY_POLICY = """
Shell Keyboard Privacy Policy

Last updated: 2026-09-06

1. DATA COLLECTION
Shell Keyboard does NOT collect any personal data.

2. DATA STORAGE
- All input data is processed locally on your device
- User dictionary is stored locally in a SQLite database
- Preferences are stored locally using Android DataStore

3. DATA TRANSMISSION
- By default, NO data is transmitted anywhere
- Optional LAN sync transfers user dictionary between your own devices
- LAN sync uses local network only (no internet)

4. PASSWORD FIELDS
- When typing in password fields, the candidate bar is hidden
- Password field input is never logged or stored
- No key logging occurs in any input field

5. THIRD PARTY SERVICES
- Shell Keyboard uses the RIME input engine (librime)
- RIME is fully offline and open source
- No analytics, no crash reporting, no telemetry

6. PERMISSIONS
- VIBRATE: For haptic feedback (optional)
- INTERNET: Only for LAN sync (optional, disabled by default)

7. CONTACT
This is an open source project. Source code is available on GitHub.
"""
    }
}
