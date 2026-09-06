package com.justtype.shellkeyboard.sync

/**
 * Merges user dictionaries from multiple devices.
 * 
 * Strategy: Last-Write-Wins (LWW) based on timestamp.
 * This is simple and works well for personal use across devices.
 */
class DictMerge {

    /**
     * Merge two dictionaries, keeping the most recent entry for each word.
     */
    fun merge(
        local: List<DictEntry>,
        remote: List<DictEntry>
    ): List<DictEntry> {
        val merged = mutableMapOf<String, DictEntry>()

        // Add all local entries
        local.forEach { merged[it.word] = it }

        // Merge remote entries (LWW)
        remote.forEach { remoteEntry ->
            val localEntry = merged[remoteEntry.word]
            if (localEntry == null || remoteEntry.timestamp > localEntry.timestamp) {
                merged[remoteEntry.word] = remoteEntry
            }
        }

        return merged.values.toList().sortedByDescending { it.timestamp }
    }

    data class DictEntry(
        val word: String,
        val frequency: Int,
        val timestamp: Long
    )
}
