package com.justtype.shellkeyboard.keyboard.ui

import android.content.Context

/**
 * Manages keyboard view switching based on schema and input type.
 * 
 * Handles transitions between:
 * - QWERTY keyboard
 * - Symbol keyboard
 * - Number pad
 * - Programmer keyboard
 */
class KeyboardWindow(private val context: Context) {

    enum class KeyboardType {
        QWERTY,
        SYMBOLS,
        NUMPAD,
        PROGRAMMER
    }

    private var currentType: KeyboardType = KeyboardType.QWERTY

    fun switchTo(type: KeyboardType) {
        if (currentType == type) return
        currentType = type
        // Trigger view rebuild
    }

    fun getCurrentType(): KeyboardType = currentType
}
