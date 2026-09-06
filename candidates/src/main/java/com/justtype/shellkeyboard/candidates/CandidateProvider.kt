package com.justtype.shellkeyboard.candidates

import com.justtype.shellkeyboard.core.Rime
import com.justtype.shellkeyboard.core.RimeSession

/**
 * Provides candidate words from RIME engine.
 * 
 * Handles:
 * - Fetching candidates from RIME context
 * - Caching for performance
 * - Fallback when RIME is not available
 */
class CandidateProvider {

    private var session: RimeSession? = null

    fun setSession(session: RimeSession?) {
        this.session = session
    }

    /**
     * Get current candidate list.
     */
    fun getCandidates(): List<Candidate> {
        val ctx = session?.getContext() ?: return emptyList()
        return ctx.menu.candidates.map { c ->
            Candidate(
                text = c.text,
                comment = c.comment,
                index = ctx.menu.candidates.indexOf(c)
            )
        }
    }

    /**
     * Get highlighted candidate index.
     */
    fun getHighlightedIndex(): Int {
        return session?.getContext()?.menu?.highlightedCandidateIndex ?: 0
    }

    /**
     * Check if there are more pages.
     */
    fun hasNextPage(): Boolean {
        return !(session?.getContext()?.menu?.isLastPage ?: true)
    }

    data class Candidate(
        val text: String,
        val comment: String,
        val index: Int
    )
}
