package com.justtype.shellkeyboard.candidates

import com.justtype.shellkeyboard.core.RimeSession

/**
 * Handles word association (联想词).
 * 
 * After committing a word, RIME can suggest associated words
 * based on the current context.
 */
class AssociateWord {

    private var isAssociating: Boolean = false
    private var associatedWords: List<String> = emptyList()
    private var session: RimeSession? = null

    fun setSession(session: RimeSession?) {
        this.session = session
    }

    /**
     * Start association mode with a committed word.
     */
    fun startAssociation(word: String) {
        isAssociating = true
        // Query RIME for associated words
        associatedWords = queryAssociativeWords(word)
    }

    /**
     * Get current associated words.
     */
    fun getAssociatedWords(): List<String> {
        return if (isAssociating) associatedWords else emptyList()
    }

    /**
     * Check if currently in association mode.
     */
    fun isAssociating(): Boolean = isAssociating

    /**
     * Exit association mode.
     */
    fun stopAssociation() {
        isAssociating = false
        associatedWords = emptyList()
    }

    /**
     * Query RIME for associative words.
     */
    private fun queryAssociativeWords(word: String): List<String> {
        // In a real implementation, this would query RIME's associative dictionary
        // For now, return empty list
        return emptyList()
    }
}
