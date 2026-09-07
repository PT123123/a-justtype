package com.justtype.shellkeyboard.keyboard.ui

import android.view.inputmethod.InputConnection
import android.view.inputmethod.ExtractedTextRequest

class InputConnectionBridge {
    var currentInputConnection: InputConnection? = null

    fun commitText(text: String, newCursorPosition: Int = 1) {
        currentInputConnection?.commitText(text, newCursorPosition)
    }

    fun setComposingText(text: String, newCursorPosition: Int = 1) {
        currentInputConnection?.setComposingText(text, newCursorPosition)
    }

    fun deleteSurroundingText(beforeLength: Int, afterLength: Int = 0) {
        currentInputConnection?.deleteSurroundingText(beforeLength, afterLength)
    }

    fun moveCursor(offset: Int) {
        val ic = currentInputConnection ?: return
        val extracted = ic.getExtractedText(ExtractedTextRequest(), 0)
        if (extracted != null) {
            val newPos = (extracted.selectionStart + offset).coerceIn(0, extracted.text.length)
            ic.setSelection(newPos, newPos)
        }
    }

    fun getTextBeforeCursor(maxChars: Int = 100): CharSequence? {
        return currentInputConnection?.getTextBeforeCursor(maxChars, 0)
    }

    fun getTextAfterCursor(maxChars: Int = 100): CharSequence? {
        return currentInputConnection?.getTextAfterCursor(maxChars, 0)
    }

    fun sendEnter() {
        currentInputConnection?.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER))
        currentInputConnection?.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_ENTER))
    }

    fun finishComposing() {
        currentInputConnection?.finishComposingText()
    }
}
