package com.justtype.shellkeyboard.dict

import com.justtype.shellkeyboard.data.UserDictDb
import com.justtype.shellkeyboard.data.UserWord

/**
 * Repository for user dictionary operations.
 * 
 * Handles:
 * - Adding user-typed words
 * - Removing words
 * - Exporting/importing user dictionary
 * - Syncing with RIME's user dictionary
 * - Self-learning: increase frequency when word is selected
 */
class UserDictRepository(private val db: UserDictDb) {

    /**
     * Add a word to user dictionary.
     */
    suspend fun addWord(word: String, frequency: Int = 1) {
        val existing = db.userWordDao().findByWord(word)
        if (existing != null) {
            db.userWordDao().insert(existing.copy(frequency = existing.frequency + 1, lastUsed = System.currentTimeMillis()))
        } else {
            db.userWordDao().insert(UserWord(word, frequency, System.currentTimeMillis()))
        }
    }

    /**
     * Remove a word from user dictionary.
     */
    suspend fun removeWord(word: String) {
        val existing = db.userWordDao().findByWord(word)
        if (existing != null) {
            db.userWordDao().delete(existing)
        }
    }

    /**
     * Get all user words.
     */
    suspend fun getAllWords(): List<UserWord> {
        return db.userWordDao().getAll()
    }

    /**
     * Export user dictionary to file.
     */
    suspend fun exportToFile(path: String) {
        val words = getAllWords()
        val content = words.joinToString("\n") { "${it.word}	${it.frequency}" }
        java.io.File(path).writeText(content)
    }

    /**
     * Import user dictionary from file.
     */
    suspend fun importFromFile(path: String) {
        val file = java.io.File(path)
        if (!file.exists()) return
        val lines = file.readLines()
        lines.forEach { line ->
            val parts = line.split("\t")
            if (parts.size >= 2) {
                val word = parts[0]
                val freq = parts[1].toIntOrNull() ?: 1
                addWord(word, freq)
            }
        }
    }

    /**
     * Increase word frequency (self-learning).
     */
    suspend fun increaseFrequency(word: String) {
        addWord(word, 1)
    }
}
