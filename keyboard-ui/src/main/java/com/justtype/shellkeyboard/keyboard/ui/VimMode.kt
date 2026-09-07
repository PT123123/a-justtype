package com.justtype.shellkeyboard.keyboard.ui

import android.view.KeyEvent
import android.os.SystemClock
import com.justtype.shellkeyboard.keyboard.ui.InputConnectionBridge

class VimMode(private val bridge: InputConnectionBridge) {

    var isEnabled: Boolean = false
        private set

    private var isNormalMode = true

    fun toggle() {
        isEnabled = !isNormalMode
        isNormalMode = !isNormalMode
    }

    fun onKeyPress(keyCode: Int): Boolean {
        if (!isEnabled) return false

        when (keyCode) {
            KeyEvent.KEYCODE_H -> { bridge.moveCursor(-1); return true }
            KeyEvent.KEYCODE_J -> { moveDown(); return true }
            KeyEvent.KEYCODE_K -> { moveUp(); return true }
            KeyEvent.KEYCODE_L -> { bridge.moveCursor(1); return true }
            KeyEvent.KEYCODE_W -> { moveToNextWord(); return true }
            KeyEvent.KEYCODE_B -> { moveToPrevWord(); return true }
            KeyEvent.KEYCODE_0 -> { moveToLineStart(); return true }
            KeyEvent.KEYCODE_4 -> { moveToLineEnd(); return true }
            KeyEvent.KEYCODE_U -> { undo(); return true }
            else -> return false
        }
    }

    private fun moveDown() {
        val text = bridge.getTextAfterCursor(500) ?: return
        val lines = text.toString().split("\n")
        if (lines.size > 1) {
            bridge.moveCursor(lines[0].length + 1)
        }
    }

    private fun moveUp() {
        val text = bridge.getTextBeforeCursor(500) ?: return
        val lines = text.toString().split("\n")
        if (lines.size > 1) {
            val prevLineLength = lines[lines.size - 2].length
            bridge.moveCursor(-(prevLineLength + 1))
        }
    }

    private fun moveToNextWord() {
        val text = bridge.getTextAfterCursor(200) ?: return
        val str = text.toString()
        val match = Regex("\\s+\\S").find(str)
        if (match != null) {
            bridge.moveCursor(match.range.first + 1)
        }
    }

    private fun moveToPrevWord() {
        val text = bridge.getTextBeforeCursor(200) ?: return
        val str = text.toString()
        val matches = Regex("\\S\\s+").findAll(str).toList()
        if (matches.isNotEmpty()) {
            bridge.moveCursor(-(str.length - matches.last().range.first - 1))
        }
    }

    private fun moveToLineStart() {
        val text = bridge.getTextBeforeCursor(500) ?: return
        val lines = text.toString().split("\n")
        if (lines.size > 1) {
            bridge.moveCursor(-(lines.last().length))
        }
    }

    private fun moveToLineEnd() {
        val text = bridge.getTextAfterCursor(500) ?: return
        val lines = text.toString().split("\n")
        if (lines.size > 1) {
            bridge.moveCursor(lines[0].length)
        }
    }

    private fun undo() {
        val ic = bridge.currentInputConnection ?: return
        val now = SystemClock.uptimeMillis()
        ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, 0, 0))
        ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_Z, 0, 0))
    }
}
