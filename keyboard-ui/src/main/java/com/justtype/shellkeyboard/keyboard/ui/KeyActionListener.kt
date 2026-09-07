package com.justtype.shellkeyboard.keyboard.ui

import android.view.KeyEvent
import com.justtype.shellkeyboard.core.RimeDispatcher

class KeyActionListener(
    private val rimeDispatcher: RimeDispatcher,
    private val inputConnectionBridge: InputConnectionBridge,
    private val vimMode: VimMode? = null,
    private val bracketPair: BracketPair? = null,
    private val markdownShortcuts: MarkdownShortcuts? = null,
    private val onRimeKeyProcessed: ((Boolean) -> Unit)? = null
) {
    fun onKeyPress(keyCode: Int, modifiers: Int = 0): Boolean {
        if (vimMode?.isEnabled == true) {
            if (vimMode.onKeyPress(keyCode)) return true
        }
        when (keyCode) {
            KeyEvent.KEYCODE_DEL -> { inputConnectionBridge.deleteSurroundingText(1, 0); return true }
            KeyEvent.KEYCODE_ENTER -> { inputConnectionBridge.sendEnter(); return true }
            KeyEvent.KEYCODE_SPACE -> { onRimeKeyProcessed?.invoke(false); inputConnectionBridge.commitText(" "); return true }
        }
        markdownShortcuts?.let { md ->
            val char = getCharForKeyCode(keyCode, modifiers)
            if (char != null && md.process(char)) return true
        }
        bracketPair?.let { bp ->
            val char = getCharForKeyCode(keyCode, modifiers)
            if (char != null && bp.process(char)) return true
        }
        val char = getCharForKeyCode(keyCode, modifiers)
        if (char != null) {
            rimeDispatcher.execute { }
            inputConnectionBridge.commitText(char)
            onRimeKeyProcessed?.invoke(true)
            return true
        }
        return false
    }

    private fun getCharForKeyCode(keyCode: Int, modifiers: Int): String? {
        val isShift = modifiers and KeyEvent.META_SHIFT_ON != 0
        return when (keyCode) {
            in KeyEvent.KEYCODE_A..KeyEvent.KEYCODE_Z -> { val base = if (isShift) 'A' else 'a'; (base + (keyCode - KeyEvent.KEYCODE_A)).toString() }
            in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> { val chars = if (isShift) ")!@#$%^&*(" else "0123456789"; chars[keyCode - KeyEvent.KEYCODE_0].toString() }
            KeyEvent.KEYCODE_PERIOD -> "."
            KeyEvent.KEYCODE_COMMA -> ","
            KeyEvent.KEYCODE_MINUS -> if (isShift) "_" else "-"
            KeyEvent.KEYCODE_SLASH -> if (isShift) "?" else "/"
            KeyEvent.KEYCODE_SEMICOLON -> if (isShift) ":" else ";"
            KeyEvent.KEYCODE_APOSTROPHE -> if (isShift) "\\\"" else "'"
            KeyEvent.KEYCODE_GRAVE -> if (isShift) "~" else "`"
            KeyEvent.KEYCODE_LEFT_BRACKET -> if (isShift) "{" else "["
            KeyEvent.KEYCODE_RIGHT_BRACKET -> if (isShift) "}" else "]"
            KeyEvent.KEYCODE_BACKSLASH -> if (isShift) "|" else "\\"
            KeyEvent.KEYCODE_EQUALS -> if (isShift) "+" else "="
            else -> null
        }
    }
}
