package com.justtype.shellkeyboard.settings

import android.content.Context
import android.media.AudioManager
import android.view.KeyEvent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager

/**
 * Manages haptic and audio feedback for key presses.
 */
class FeedbackManager(private val context: Context) {

    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        manager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    /**
     * Trigger haptic feedback on key press.
     */
    fun vibrate(durationMs: Long = 20) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(durationMs)
        }
    }

    /**
     * Play click sound on key press.
     */
    fun playClick() {
        audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, 1.0f)
    }

    /**
     * Check if haptic feedback is enabled.
     */
    fun isHapticEnabled(): Boolean {
        return true // TODO: Load from preferences
    }

    /**
     * Check if sound feedback is enabled.
     */
    fun isSoundEnabled(): Boolean {
        return true // TODO: Load from preferences
    }
}
