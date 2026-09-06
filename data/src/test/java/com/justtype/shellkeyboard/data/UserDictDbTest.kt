package com.justtype.shellkeyboard.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UserDictDbTest {

    private lateinit var db: UserDictDb
    private lateinit var dao: UserWordDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDictDb::class.java
        ).allowMainThreadQueries().build()
        dao = db.userWordDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_andRetrieve() = runBlocking {
        val word = UserWord("hello", 5, 1000)
        dao.insert(word)
        
        val retrieved = dao.findByWord("hello")
        assertNotNull(retrieved)
        assertEquals("hello", retrieved!!.word)
        assertEquals(5, retrieved.frequency)
        assertEquals(1000, retrieved.lastUsed)
    }

    @Test
    fun getAll_returnsAllWords() = runBlocking {
        dao.insert(UserWord("a", 1, 100))
        dao.insert(UserWord("b", 2, 200))
        dao.insert(UserWord("c", 3, 300))
        
        val all = dao.getAll()
        assertEquals(3, all.size)
    }

    @Test
    fun getAll_sortedByFrequency() = runBlocking {
        dao.insert(UserWord("low", 1, 100))
        dao.insert(UserWord("high", 10, 200))
        dao.insert(UserWord("mid", 5, 300))
        
        val all = dao.getAll()
        assertEquals("high", all[0].word)
        assertEquals("mid", all[1].word)
        assertEquals("low", all[2].word)
    }

    @Test
    fun findByWord_notFound_returnsNull() = runBlocking {
        val result = dao.findByWord("nonexistent")
        assertNull(result)
    }

    @Test
    fun delete_removesWord() = runBlocking {
        val word = UserWord("test", 1, 100)
        dao.insert(word)
        dao.delete(word)
        
        val result = dao.findByWord("test")
        assertNull(result)
    }

    @Test
    fun deleteAll_removesAllWords() = runBlocking {
        dao.insert(UserWord("a", 1, 100))
        dao.insert(UserWord("b", 2, 200))
        dao.deleteAll()
        
        val all = dao.getAll()
        assertTrue(all.isEmpty())
    }

    @Test
    fun insert_sameWord_replaces() = runBlocking {
        dao.insert(UserWord("test", 1, 100))
        dao.insert(UserWord("test", 5, 200))
        
        val result = dao.findByWord("test")
        assertNotNull(result)
        assertEquals(5, result!!.frequency)
    }
}
