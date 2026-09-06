package com.justtype.shellkeyboard.dict

import com.justtype.shellkeyboard.data.UserDictDb
import android.content.Context

/**
 * Repository for user dictionary operations.
 * 
 * Handles:
 * - Adding user-typed words
 * - Removing words
 * - Exporting/importing user dictionary
 * - Syncing with RIME's user dictionary
 */
class UserDictRepository(private val db: UserDictDb) {

    /**
     * Add a word to user dictionary.
     */
    fun addWord(word: String, frequency: Int = 1) {
        // Insert into local DB
        // Sync to RIME user dictionary
    }

    /**
     * Remove a word from user dictionary.
     */
    fun removeWord(word: String) {
        // Remove from local DB
        // Sync to RIME user dictionary
    }

    /**
     * Get all user words.
     */
    fun getAllWords(): List<UserWord> {
        return emptyList() // TODO: Query from DB
    }

    /**
     * Export user dictionary to file.
     */
    fun exportToFile(path: String) {
        // Write words to file
    }

    /**
     * Import user dictionary from file.
     */
    fun importFromFile(path: String) {
        // Read words from file
    }

    data class UserWord(
        val word: String,
        val frequency: Int,
        val lastUsed: Long
    )
}
