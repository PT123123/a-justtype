package com.justtype.shellkeyboard.keyboard.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import com.justtype.shellkeyboard.core.RimeDispatcher
import com.justtype.shellkeyboard.ime.InputConnectionBridge

/**
 * Root view combining keyboard and candidate bar.
 * 
 * Layout:
 * ┌─────────────────────────┐
 * │     CandidatesBar       │
 * ├─────────────────────────┤
 * │                         │
 * │     KeyboardView        │
 * │                         │
 * └─────────────────────────┘
 */
class InputView(
    private val context: Context,
    private val rimeDispatcher: RimeDispatcher,
    private val inputConnectionBridge: InputConnectionBridge
) {
    val rootView: View
    private val candidatesBar: CandidatesBar
    private val keyboardView: KeyboardView
    private val keyboardWindow: KeyboardWindow

    init {
        keyboardWindow = KeyboardWindow(context)
        candidatesBar = CandidatesBar(context)
        keyboardView = KeyboardView(context, rimeDispatcher, inputConnectionBridge)
        
        // Compose layout
        val container = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(candidatesBar.view, android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            ))
            addView(keyboardView.view, android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            ))
        }
        rootView = container
    }

    fun onStartInputView(info: EditorInfo?) {
        keyboardView.onStartInput(info)
        candidatesBar.onStartInput(info)
    }

    fun onFinishInputView() {
        keyboardView.onFinishInput()
        candidatesBar.onFinishInput()
    }

    fun setPasswordMode(isPassword: Boolean) {
        candidatesBar.setPasswordMode(isPassword)
        keyboardView.setPasswordMode(isPassword)
    }
}
