package com.justtype.shellkeyboard.keyboard.ui

import android.view.KeyEvent
import com.justtype.shellkeyboard.core.RimeDispatcher
import com.justtype.shellkeyboard.ime.InputConnectionBridge

/**
 * Handles key press events and routes them to RIME or direct actions.
 * 
 * Decision flow:
 * 1. Is it a function key (backspace, enter, switch)? → Direct action
 * 2. Is it a RIME composition key (a-z, 0-9)? → RIME processKey
 * 3. Is it a symbol? → Direct commit
 */
class KeyActionListener(
    private val rimeDispatcher: RimeDispatcher,
    private val inputConnectionBridge: InputConnectionBridge
) {

    fun onKeyPress(keyCode: Int, modifiers: Int = 0) {
        when {
            // Function keys
            keyCode == KeyEvent.KEYCODE_DEL -> handleBackspace()
            keyCode == KeyEvent.KEYCODE_ENTER -> handleEnter()
            keyCode == KeyEvent.KEYCODE_SPACE -> handleSpace()
            
            // RIME composition keys (a-z)
            keyCode in KeyEvent.KEYCODE_A..KeyEvent.KEYCODE_Z -> {
                rimeDispatcher.execute {
                    // Process through RIME
                }
            }
            
            // Symbols - direct commit
            else -> {
                val char = getCharForKeyCode(keyCode, modifiers)
                if (char != null) {
                    inputConnectionBridge.commitText(char)
                }
            }
        }
    }

    private fun handleBackspace() {
        inputConnectionBridge.deleteSurroundingText(1, 0)
    }

    private fun handleEnter() {
        inputConnectionBridge.sendEnter()
    }

    private fun handleSpace() {
        // Commit first candidate or space
        inputConnectionBridge.commitText(" ")
    }

    private fun getCharForKeyCode(keyCode: Int, modifiers: Int): String? {
        val isShift = modifiers and KeyEvent.META_SHIFT_ON != 0
        return when (keyCode) {
            in KeyEvent.KEYCODE_A..KeyEvent.KEYCODE_Z -> {
                val base = if (isShift) 'A' else 'a'
                (base + (keyCode - KeyEvent.KEYCODE_A)).toString()
            }
            in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> {
                val chars = if (isShift) ")!@#$%^&*(" else "0123456789"
                chars[keyCode - KeyEvent.KEYCODE_0].toString()
            }
            else -> null
        }
    }
}
