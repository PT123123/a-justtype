package com.justtype.shellkeyboard.keyboard.ui

import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager

/**
 * URL input mode.
 * 
 * When the input field is for URLs, provides:
 * - Common TLD shortcuts (.com, .org, .net, .io, .dev)
 * - Protocol shortcuts (http://, https://, ftp://)
 * - Quick access to /, ., -, _
 */
class UrlMode {

    private val tldShortcuts = mapOf(
        ".com" to ".com",
        ".org" to ".org",
        ".net" to ".net",
        ".io" to ".io",
        ".dev" to ".dev",
        ".app" to ".app",
        ".cn" to ".cn",
        ".me" to ".me"
    )

    private val protocolShortcuts = listOf(
        "https://",
        "http://",
        "ftp://"
    )

    /**
     * Check if input field is URL type.
     */
    fun isUrlField(info: EditorInfo?): Boolean {
        if (info == null) return false
        val inputType = info.inputType and android.text.InputType.TYPE_MASK_VARIATION
        return inputType == android.text.InputType.TYPE_TEXT_VARIATION_URI ||
               inputType == android.text.InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
    }

    /**
     * Get URL-specific keyboard layout.
     */
    fun getUrlKeyboard(): List<String> {
        return listOf(
            "https://", "http://", "ftp://",
            ".com", ".org", ".net", ".io", ".dev",
            "/", ".", "-", "_", "@"
        )
    }

    /**
     * Process URL shortcut.
     */
    fun processShortcut(shortcut: String): Boolean {
        return tldShortcuts.containsKey(shortcut) || protocolShortcuts.contains(shortcut)
    }
}
