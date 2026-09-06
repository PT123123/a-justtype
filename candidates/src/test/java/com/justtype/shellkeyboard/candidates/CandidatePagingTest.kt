package com.justtype.shellkeyboard.candidates

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CandidatePagingTest {

    private lateinit var paging: CandidatePaging

    @Before
    fun setUp() {
        paging = CandidatePaging()
    }

    @Test
    fun defaultPage_isZero() {
        assertEquals(0, paging.getCurrentPage())
    }

    @Test
    fun nextPage_advances() {
        paging.setTotalPages(3)
        assertTrue(paging.nextPage())
        assertEquals(1, paging.getCurrentPage())
    }

    @Test
    fun nextPage_atLastPage_returnsFalse() {
        paging.setTotalPages(2)
        paging.nextPage()
        assertFalse(paging.nextPage())
        assertEquals(1, paging.getCurrentPage())
    }

    @Test
    fun previousPage_goesBack() {
        paging.setTotalPages(3)
        paging.nextPage()
        paging.nextPage()
        assertTrue(paging.previousPage())
        assertEquals(1, paging.getCurrentPage())
    }

    @Test
    fun previousPage_atFirstPage_returnsFalse() {
        assertFalse(paging.previousPage())
        assertEquals(0, paging.getCurrentPage())
    }

    @Test
    fun reset_clearsState() {
        paging.setTotalPages(5)
        paging.nextPage()
        paging.nextPage()
        paging.reset()
        assertEquals(0, paging.getCurrentPage())
        assertEquals(0, paging.getPageSize())
    }

    @Test
    fun defaultPageSize_isFive() {
        assertEquals(5, CandidatePaging.DEFAULT_PAGE_SIZE)
    }
}
