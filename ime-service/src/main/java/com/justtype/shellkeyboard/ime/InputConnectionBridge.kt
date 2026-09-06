package com.justtype.shellkeyboard.ime

import android.view.inputmethod.InputConnection
import android.view.inputmethod.ExtractedTextRequest

/**
 * Bridge between IME and target app via InputConnection.
 * 
 * Handles: text commit, cursor movement, selection, deletion, composing.
 */
class InputConnectionBridge {

    var currentInputConnection: InputConnection? = null

    /**
     * Commit text to the target app.
     */
    fun commitText(text: String, newCursorPosition: Int = 1) {
        currentInputConnection?.commitText(text, newCursorPosition)
    }

    /**
     * Set composing text (for RIME preedit display).
     */
    fun setComposingText(text: String, newCursorPosition: Int = 1) {
        currentInputConnection?.setComposingText(text, newCursorPosition)
    }

    /**
     * Delete text before cursor (backspace).
     */
    fun deleteSurroundingText(beforeLength: Int, afterLength: Int = 0) {
        currentInputConnection?.deleteSurroundingText(beforeLength, afterLength)
    }

    /**
     * Move cursor.
     */
    fun moveCursor(offset: Int) {
        val ic = currentInputConnection ?: return
        val extracted = ic.getExtractedText(ExtractedTextRequest(), 0)
        if (extracted != null) {
            val newPos = (extracted.selectionStart + offset).coerceIn(0, extracted.text.length)
            ic.setSelection(newPos, newPos)
        }
    }

    /**
     * Get text before cursor.
     */
    fun getTextBeforeCursor(maxChars: Int = 100): CharSequence? {
        return currentInputConnection?.getTextBeforeCursor(maxChars, 0)
    }

    /**
     * Get text after cursor.
     */
    fun getTextAfterCursor(maxChars: Int = 100): CharSequence? {
        return currentInputConnection?.getTextAfterCursor(maxChars, 0)
    }

    /**
     * Send enter key action.
     */
    fun sendEnter() {
        currentInputConnection?.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER))
        currentInputConnection?.sendKeyEvent(android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_ENTER))
    }

    /**
     * Finish composing (clear preedit).
     */
    fun finishComposing() {
        currentInputConnection?.finishComposingText()
    }
}
