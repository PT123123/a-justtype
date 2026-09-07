package com.justtype.shellkeyboard.keyboard.ui

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.justtype.shellkeyboard.core.RimeDispatcher
import com.justtype.shellkeyboard.candidates.AssociateWord
import com.justtype.shellkeyboard.candidates.CandidateProvider
import com.justtype.shellkeyboard.keyboard.ui.KeyboardWindow.KeyboardType

class InputView(
    private val context: Context,
    private val rimeDispatcher: RimeDispatcher,
    private val inputConnectionBridge: InputConnectionBridge
) {
    val rootView: View
    private val candidatesBar = CandidatesBar(context)
    private val keyboardView: KeyboardView
    private val keyboardWindow = KeyboardWindow(context)
    private val emojiPanel = EmojiPanel(context)
    private val extractMode = ExtractMode(context)
    private val clipboardManager = ClipboardManager(context)
    private val associateWord = AssociateWord()
    private val candidateProvider = CandidateProvider()
    
    private var isPasswordMode = false
    private var isLandscape = false
    private var isEmojiMode = false
    private var isExtractMode = false
    private var currentCandidates = listOf<String>()
    private var currentSchemaLabel = "拼音"
    
    init {
        candidatesBar.onCandidateSelected = { index -> onCandidateChosen(index) }
        candidatesBar.onNextPage = { }
        candidatesBar.onPrevPage = { }
        
        emojiPanel.onEmojiSelected = { emoji ->
            inputConnectionBridge.commitText(emoji)
        }
        
        inputConnectionBridge.onTextCommitted = { text ->
            clipboardManager.copy(text)
        }
        
        inputConnectionBridge.onComposingChanged = { text ->
            if (text.isEmpty()) {
                candidatesBar.updateCandidates(emptyList())
            }
        }
        
        keyboardView = KeyboardView(context, rimeDispatcher, inputConnectionBridge)
        
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        
        extractMode.view.visibility = View.GONE
        container.addView(extractMode.view, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))
        
        candidatesBar.view.visibility = View.GONE
        container.addView(candidatesBar.view, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))
        
        emojiPanel.view.visibility = View.GONE
        container.addView(emojiPanel.view, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))
        
        container.addView(keyboardView.view, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))
        
        rootView = container
        
        isLandscape = context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        updateLayoutMode()
    }
    
    private fun updateLayoutMode() {
        if (isLandscape && isExtractMode) {
            extractMode.view.visibility = View.VISIBLE
            candidatesBar.view.visibility = if (isPasswordMode) View.GONE else View.VISIBLE
        } else {
            extractMode.view.visibility = View.GONE
            candidatesBar.view.visibility = if (isPasswordMode) View.GONE else View.VISIBLE
        }
    }
    
    private fun onCandidateChosen(index: Int) {
        if (index < currentCandidates.size) {
            val word = currentCandidates[index]
            inputConnectionBridge.commitText(word)
            inputConnectionBridge.onTextCommitted?.invoke(word)
            associateWord.startAssociation(word)
        }
    }
    
    fun onStartInputView(info: EditorInfo?) {
        keyboardView.onStartInput(info)
        candidatesBar.onStartInput(info)
        
        val urlMode = UrlMode()
        if (urlMode.isUrlField(info)) {
            keyboardView.switchKeyboardType(KeyboardType.SYMBOLS)
        }
    }
    
    fun onFinishInputView() {
        keyboardView.onFinishInput()
        candidatesBar.onFinishInput()
        isEmojiMode = false
        emojiPanel.view.visibility = View.GONE
    }
    
    fun setPasswordMode(isPassword: Boolean) {
        isPasswordMode = isPassword
        candidatesBar.setPasswordMode(isPassword)
        keyboardView.setPasswordMode(isPassword)
        updateLayoutMode()
    }
    
    fun setLandscapeMode(isLandscape: Boolean) {
        this.isLandscape = isLandscape
        this.isExtractMode = isLandscape
        updateLayoutMode()
    }
    
    fun toggleEmojiMode() {
        isEmojiMode = !isEmojiMode
        emojiPanel.view.visibility = if (isEmojiMode) View.VISIBLE else View.GONE
        keyboardView.view.visibility = if (isEmojiMode) View.GONE else View.VISIBLE
    }
    
    fun showClipboardHistory() {
        val history = clipboardManager.getHistory()
        if (history.isNotEmpty()) {
            candidatesBar.updateCandidates(history, 0, "剪贴板")
        }
    }
    
    fun getClipboardManager(): ClipboardManager = clipboardManager
    fun getKeyboardView(): KeyboardView = keyboardView
    fun getCandidatesBar(): CandidatesBar = candidatesBar
    fun getEmojiPanel(): EmojiPanel = emojiPanel
    fun getExtractMode(): ExtractMode = extractMode
}
