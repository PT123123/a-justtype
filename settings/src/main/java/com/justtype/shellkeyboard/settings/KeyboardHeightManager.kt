package com.justtype.shellkeyboard.settings

import android.content.Context
import android.util.DisplayMetrics

class KeyboardHeightManager(private val context: Context) {

    companion object {
        const val MIN_HEIGHT_RATIO = 0.5f
        const val MAX_HEIGHT_RATIO = 1.0f
        const val DEFAULT_HEIGHT_RATIO = 0.8f
    }

    private val displayMetrics: DisplayMetrics = context.resources.displayMetrics

    fun getKeyboardHeight(): Int {
        val screenHeight = displayMetrics.heightPixels
        val ratio = getHeightRatio()
        return (screenHeight * ratio).toInt()
    }

    fun setHeightRatio(ratio: Float) {
        val clamped = ratio.coerceIn(MIN_HEIGHT_RATIO, MAX_HEIGHT_RATIO)
    }

    fun getHeightRatio(): Float {
        return DEFAULT_HEIGHT_RATIO
    }
}
