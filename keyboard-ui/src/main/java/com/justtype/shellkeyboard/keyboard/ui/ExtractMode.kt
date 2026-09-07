package com.justtype.shellkeyboard.keyboard.ui

import android.content.Context
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout

/**
 * Extract mode for landscape editing.
 * 
 * When the device is in landscape mode, shows a full-screen
 * text editing area above the keyboard.
 */
class ExtractMode(private val context: Context) {

    val view: android.view.View
    private val editText: EditText

    init {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        editText = EditText(context).apply {
            isSingleLine = false
            minLines = 3
            maxLines = 10
            hint = "Edit text here..."
        }
        layout.addView(editText, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))
        view = layout
    }

    /**
     * Get the edited text.
     */
    fun getText(): String = editText.text.toString()

    /**
     * Set the text to edit.
     */
    fun setText(text: String) {
        editText.setText(text)
        editText.setSelection(text.length)
    }

    /**
     * Append text.
     */
    fun appendText(text: String) {
        editText.append(text)
    }

    /**
     * Clear text.
     */
    fun clear() {
        editText.text.clear()
    }
}
