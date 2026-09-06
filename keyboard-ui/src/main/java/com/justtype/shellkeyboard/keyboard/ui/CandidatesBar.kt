package com.justtype.shellkeyboard.keyboard.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.inputmethod.EditorInfo

/**
 * Horizontal scrolling candidate bar.
 * 
 * Displays RIME candidates with:
 * - Page navigation (prev/next)
 * - Candidate selection
 * - Gesture support (swipe to select)
 */
class CandidatesBar(private val context: Context) {
    val view: View
    private val scrollView: HorizontalScrollView
    private val container: LinearLayout
    private var isPasswordMode = false

    init {
        scrollView = HorizontalScrollView(context).apply {
            isHorizontalScrollBarEnabled = false
        }
        container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        scrollView.addView(container)
        view = scrollView
    }

    fun updateCandidates(candidates: List<String>, highlightedIndex: Int = 0) {
        if (isPasswordMode) {
            view.visibility = View.GONE
            return
        }
        view.visibility = View.VISIBLE
        container.removeAllViews()
        
        candidates.forEachIndexed { index, candidate ->
            val tv = TextView(context).apply {
                text = candidate
                textSize = 18f
                setPadding(32, 16, 32, 16)
                setOnClickListener { onCandidateSelected(index) }
            }
            container.addView(tv)
        }
    }

    fun onStartInput(info: EditorInfo?) {
        // Reset state
    }

    fun onFinishInput() {
        container.removeAllViews()
    }

    fun setPasswordMode(isPassword: Boolean) {
        isPasswordMode = isPassword
        view.visibility = if (isPassword) View.GONE else View.VISIBLE
    }

    private fun onCandidateSelected(index: Int) {
        // Commit selected candidate
    }
}
