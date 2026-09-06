package com.justtype.shellkeyboard.keyboard.ui

import android.content.Context
import android.view.View
import android.widget.PopupWindow

/**
 * Handles long-press popups for symbols and alternate characters.
 * 
 * Example: Long-press 'a' → popup shows à, á, â, ã, ä, å
 */
class PopupDelegate(private val context: Context) {

    private var popup: PopupWindow? = null

    fun showPopup(anchor: View, symbols: List<String>) {
        // Build popup content
        val content = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.HORIZONTAL
            symbols.forEach { symbol ->
                val tv = android.widget.TextView(context).apply {
                    text = symbol
                    textSize = 24f
                    setPadding(24, 16, 24, 16)
                    setOnClickListener {
                        onSymbolSelected(symbol)
                        dismiss()
                    }
                }
                addView(tv)
            }
        }

        popup = PopupWindow(
            content,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            isOutsideTouchable = true
            showAsDropDown(anchor, 0, -anchor.height * 2, android.view.Gravity.TOP)
        }
    }

    fun dismiss() {
        popup?.dismiss()
        popup = null
    }

    private fun onSymbolSelected(symbol: String) {
        // Commit symbol
    }
}
