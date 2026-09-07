package com.justtype.shellkeyboard.keyboard.ui

import android.view.inputmethod.InputConnection
import android.view.inputmethod.ExtractedTextRequest

class InputConnectionBridge {
    var currentInputConnection: InputConnection? = null
    
    /** Called when a candidate word is selected. */
    var onCandidateSelected: ((String) -> Unit)? = null
    
    /** Called when text is committed (for self-learning). */
    var onTextCommitted: ((String) -> Unit)? = null
    
    /** Called when composing text changes. */
    var onComposingChanged: ((String) -> Unit)? = null

    fun commitText(text: String, newCursorPosition: Int = 1) {
        currentInputConnection?.commitText(text, newCursorPosition)
        if (text.isNotEmpty() && text.length > 1) {
            onTextCommitted?.invoke(text)
        }
    }

    fun setComposingText(text: String, newCursorPosition: Int = 1) {
        currentInputConnection?.setComposingText(text, newCursorPosition)
        onComposingChanged?.invoke(text)
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
    
    fun sendKey(keyCode: Int) {
        val now = android.os.SystemClock.uptimeMillis()
        currentInputConnection?.sendKeyEvent(android.view.KeyEvent(now, now, android.view.KeyEvent.ACTION_DOWN, keyCode, 0, 0))
        currentInputConnection?.sendKeyEvent(android.view.KeyEvent(now, now, android.view.KeyEvent.ACTION_UP, keyCode, 0, 0))
    }
    
    fun setSelection(start: Int, end: Int) {
        currentInputConnection?.setSelection(start, end)
    }
    
    fun getSelectedText(): CharSequence? {
        return currentInputConnection?.getSelectedText(0)
    }
    
    fun performContextMenuAction(actionId: Int) {
        currentInputConnection?.performContextMenuAction(actionId)
    }

    fun finishComposing() {
        currentInputConnection?.finishComposingText()
    }
}
