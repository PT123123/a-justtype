package com.justtype.shellkeyboard.keyboard.ui

import android.view.KeyEvent
import com.justtype.shellkeyboard.core.RimeDispatcher
import com.justtype.shellkeyboard.keyboard.ui.InputConnectionBridge

class KeyActionListener(
    private val rimeDispatcher: RimeDispatcher,
    private val inputConnectionBridge: InputConnectionBridge
) {
    fun onKeyPress(keyCode: Int, modifiers: Int = 0) {
        when {
            keyCode == KeyEvent.KEYCODE_DEL -> inputConnectionBridge.deleteSurroundingText(1, 0)
            keyCode == KeyEvent.KEYCODE_ENTER -> inputConnectionBridge.sendEnter()
            keyCode == KeyEvent.KEYCODE_SPACE -> inputConnectionBridge.commitText(" ")
            keyCode in KeyEvent.KEYCODE_A..KeyEvent.KEYCODE_Z -> rimeDispatcher.execute { }
            else -> { val char = getCharForKeyCode(keyCode, modifiers); if (char != null) inputConnectionBridge.commitText(char) }
        }
    }

    private fun getCharForKeyCode(keyCode: Int, modifiers: Int): String? {
        val isShift = modifiers and KeyEvent.META_SHIFT_ON != 0
        return when (keyCode) {
            in KeyEvent.KEYCODE_A..KeyEvent.KEYCODE_Z -> { val base = if (isShift) 'A' else 'a'; (base + (keyCode - KeyEvent.KEYCODE_A)).toString() }
            in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> { val chars = if (isShift) ")!@#$%^&*(" else "0123456789"; chars[keyCode - KeyEvent.KEYCODE_0].toString() }
            else -> null
        }
    }
}
