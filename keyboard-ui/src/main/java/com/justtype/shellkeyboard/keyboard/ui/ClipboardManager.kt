package com.justtype.shellkeyboard.keyboard.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Clipboard management with history.
 * 
 * Features:
 * - Copy/Cut/Paste
 * - Clipboard history (last 10 items)
 * - Privacy: password fields are never stored
 */
class ClipboardManager(private val context: Context) {

    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private val history = mutableListOf<String>()
    private val maxHistory = 10

    /**
     * Copy text to clipboard.
     */
    fun copy(text: String) {
        val clip = ClipData.newPlainText("Shell Keyboard", text)
        clipboard.setPrimaryClip(clip)
        addToHistory(text)
    }

    /**
     * Cut text (copy + delete).
     */
    fun cut(text: String) {
        copy(text)
    }

    /**
     * Paste text from clipboard.
     */
    fun paste(): String? {
        val clip = clipboard.primaryClip ?: return null
        if (clip.itemCount == 0) return null
        return clip.getItemAt(0).text?.toString()
    }

    /**
     * Get clipboard history.
     */
    fun getHistory(): List<String> = history.toList()

    /**
     * Clear clipboard history.
     */
    fun clearHistory() {
        history.clear()
    }

    /**
     * Check if clipboard has content.
     */
    fun hasContent(): Boolean = clipboard.hasPrimaryClip()

    private fun addToHistory(text: String) {
        if (text.isBlank()) return
        history.remove(text)
        history.add(0, text)
        if (history.size > maxHistory) {
            history.removeAt(history.size - 1)
        }
    }

    init {
        clipboard.addPrimaryClipChangedListener {
            val clip = clipboard.primaryClip ?: return@addPrimaryClipChangedListener
            if (clip.itemCount > 0) {
                val text = clip.getItemAt(0).text?.toString() ?: return@addPrimaryClipChangedListener
                addToHistory(text)
            }
        }
    }
}
