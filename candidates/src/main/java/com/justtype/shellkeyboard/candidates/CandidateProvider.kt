package com.justtype.shellkeyboard.candidates

import com.justtype.shellkeyboard.core.Rime
import com.justtype.shellkeyboard.core.RimeSession

class CandidateProvider {

    private var session: RimeSession? = null

    fun setSession(session: RimeSession?) {
        this.session = session
    }

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

    fun getHighlightedIndex(): Int {
        return session?.getContext()?.menu?.highlightedCandidateIndex ?: 0
    }

    fun hasNextPage(): Boolean {
        return !(session?.getContext()?.menu?.isLastPage ?: true)
    }

    data class Candidate(
        val text: String,
        val comment: String,
        val index: Int
    )
}
