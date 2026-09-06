package com.justtype.shellkeyboard.candidates

/**
 * Handles word association (联想词).
 * 
 * After committing a word, RIME can suggest associated words
 * based on the current context.
 */
class AssociateWord {

    private var isAssociating: Boolean = false
    private var associatedWords: List<String> = emptyList()

    /**
     * Start association mode with a committed word.
     */
    fun startAssociation(word: String) {
        isAssociating = true
        // TODO: Query RIME for associated words
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
}
