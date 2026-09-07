package com.justtype.shellkeyboard.ime

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import com.justtype.shellkeyboard.core.Rime
import com.justtype.shellkeyboard.core.RimeDispatcher
import com.justtype.shellkeyboard.core.RimeLifecycle
import com.justtype.shellkeyboard.keyboard.ui.InputView
import com.justtype.shellkeyboard.keyboard.ui.InputConnectionBridge
import com.justtype.shellkeyboard.settings.ThemeManager

/**
 * Main IME service for Shell Keyboard.
 * 
 * Lifecycle:
 * - onCreate(): Initialize RIME engine, load config
 * - onCreateInputView(): Build keyboard UI
 * - onStartInputView(): Prepare for input (detect field type)
 * - onFinishInputView(): Cleanup per-input state
 * - onDestroy(): Release RIME engine
 */
class ShellInputMethodService : InputMethodService() {

    private lateinit var rimeDispatcher: RimeDispatcher
    private lateinit var lifecycleManager: LifecycleManager
    private lateinit var inputConnectionBridge: InputConnectionBridge
    private var inputView: InputView? = null

    override fun onCreate() {
        super.onCreate()
        rimeDispatcher = RimeDispatcher()
        lifecycleManager = LifecycleManager(rimeDispatcher)
        inputConnectionBridge = InputConnectionBridge()
        lifecycleManager.initialize()
    }

    override fun onCreateInputView(): View {
        inputView = InputView(this, rimeDispatcher, inputConnectionBridge)
        return inputView!!.rootView
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        inputView?.onStartInputView(info)
        inputConnectionBridge.currentInputConnection = currentInputConnection
        
        // Privacy: disable candidate bar for password fields
        val isPasswordField = info?.inputType?.let { type ->
            (type and EditorInfo.TYPE_MASK_CLASS) == EditorInfo.TYPE_CLASS_TEXT &&
            ((type and EditorInfo.TYPE_MASK_VARIATION) == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD ||
             (type and EditorInfo.TYPE_MASK_VARIATION) == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
             (type and EditorInfo.TYPE_MASK_VARIATION) == EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
        } ?: false
        
        inputView?.setPasswordMode(isPasswordField)
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        inputView?.onFinishInputView()
    }

    override fun onDestroy() {
        lifecycleManager.destroy()
        super.onDestroy()
    }
}
