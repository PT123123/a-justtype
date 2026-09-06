package com.justtype.shellkeyboard.dict

import com.justtype.shellkeyboard.core.Rime
import com.justtype.shellkeyboard.core.RimeSession
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class SchemaManagerTest {

    private lateinit var schemaManager: SchemaManager

    @Before
    fun setUp() {
        schemaManager = SchemaManager()
    }

    @Test
    fun initially_noCurrentSchema() {
        assertEquals("", schemaManager.getCurrentSchemaId())
    }

    @Test
    fun getAvailableSchemas_initiallyEmpty() {
        assertTrue(schemaManager.getAvailableSchemas().isEmpty())
    }

    @Test
    fun switchSchema_nullSession_returnsFalse() {
        val result = schemaManager.switchSchema("test", null)
        assertFalse(result)
    }

    @Test
    fun switchSchema_success_updatesCurrentSchema() {
        val mockSession = mock(RimeSession::class.java)
        `when`(mockSession.setSchema("test")).thenReturn(true)
        
        val result = schemaManager.switchSchema("test", mockSession)
        assertTrue(result)
        assertEquals("test", schemaManager.getCurrentSchemaId())
    }

    @Test
    fun switchSchema_failure_doesNotUpdateCurrentSchema() {
        val mockSession = mock(RimeSession::class.java)
        `when`(mockSession.setSchema("test")).thenReturn(false)
        
        val result = schemaManager.switchSchema("test", mockSession)
        assertFalse(result)
        assertEquals("", schemaManager.getCurrentSchemaId())
    }

    @Test
    fun getSchemaName_unknownId_returnsId() {
        assertEquals("unknown", schemaManager.getSchemaName("unknown"))
    }
}
