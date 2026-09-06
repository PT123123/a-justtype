package com.justtype.shellkeyboard.sync

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DictMergeTest {

    private lateinit var dictMerge: DictMerge

    @Before
    fun setUp() {
        dictMerge = DictMerge()
    }

    @Test
    fun merge_bothEmpty_returnsEmpty() {
        val result = dictMerge.merge(emptyList(), emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun merge_localOnly_returnsLocal() {
        val local = listOf(DictMerge.DictEntry("hello", 1, 1000))
        val result = dictMerge.merge(local, emptyList())
        assertEquals(1, result.size)
        assertEquals("hello", result[0].word)
    }

    @Test
    fun merge_remoteOnly_returnsRemote() {
        val remote = listOf(DictMerge.DictEntry("world", 1, 1000))
        val result = dictMerge.merge(emptyList(), remote)
        assertEquals(1, result.size)
        assertEquals("world", result[0].word)
    }

    @Test
    fun merge_noOverlap_combines() {
        val local = listOf(DictMerge.DictEntry("hello", 1, 1000))
        val remote = listOf(DictMerge.DictEntry("world", 1, 1000))
        val result = dictMerge.merge(local, remote)
        assertEquals(2, result.size)
    }

    @Test
    fun merge_overlap_keepsNewer() {
        val local = listOf(DictMerge.DictEntry("test", 1, 1000))
        val remote = listOf(DictMerge.DictEntry("test", 2, 2000))
        val result = dictMerge.merge(local, remote)
        assertEquals(1, result.size)
        assertEquals(2, result[0].frequency)
        assertEquals(2000, result[0].timestamp)
    }

    @Test
    fun merge_overlap_localNewer_keepsLocal() {
        val local = listOf(DictMerge.DictEntry("test", 3, 3000))
        val remote = listOf(DictMerge.DictEntry("test", 1, 1000))
        val result = dictMerge.merge(local, remote)
        assertEquals(1, result.size)
        assertEquals(3, result[0].frequency)
        assertEquals(3000, result[0].timestamp)
    }

    @Test
    fun merge_sortedByTimestamp() {
        val local = listOf(
            DictMerge.DictEntry("a", 1, 1000),
            DictMerge.DictEntry("b", 1, 3000)
        )
        val remote = listOf(
            DictMerge.DictEntry("c", 1, 2000)
        )
        val result = dictMerge.merge(local, remote)
        assertEquals("b", result[0].word)
        assertEquals("c", result[1].word)
        assertEquals("a", result[2].word)
    }

    @Test
    fun dictEntry_createsCorrectly() {
        val entry = DictMerge.DictEntry("test", 5, 12345)
        assertEquals("test", entry.word)
        assertEquals(5, entry.frequency)
        assertEquals(12345, entry.timestamp)
    }
}
