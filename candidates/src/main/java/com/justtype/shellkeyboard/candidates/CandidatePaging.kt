package com.justtype.shellkeyboard.candidates

/**
 * Handles candidate pagination.
 * 
 * RIME returns candidates in pages (typically 5-9 per page).
 * This class manages page navigation state.
 */
class CandidatePaging {

    companion object {
        const val DEFAULT_PAGE_SIZE = 5
    }

    private var currentPage: Int = 0
    private var totalPages: Int = 0
    private var pageSize: Int = DEFAULT_PAGE_SIZE

    fun nextPage(): Boolean {
        if (currentPage < totalPages - 1) {
            currentPage++
            return true
        }
        return false
    }

    fun previousPage(): Boolean {
        if (currentPage > 0) {
            currentPage--
            return true
        }
        return false
    }

    fun getCurrentPage(): Int = currentPage

    fun getPageSize(): Int = pageSize

    fun setTotalPages(total: Int) {
        totalPages = total
    }

    fun reset() {
        currentPage = 0
        totalPages = 0
    }
}
