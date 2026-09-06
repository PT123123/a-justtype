package com.justtype.shellkeyboard.ime

import android.view.inputmethod.InputConnection
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class InputConnectionBridgeTest {

    private lateinit var bridge: InputConnectionBridge
    private lateinit var mockConnection: InputConnection

    @Before
    fun setUp() {
        bridge = InputConnectionBridge()
        mockConnection = mock(InputConnection::class.java)
        bridge.currentInputConnection = mockConnection
    }

    @Test
    fun commitText_delegatesToConnection() {
        bridge.commitText("hello")
        verify(mockConnection).commitText("hello", 1)
    }

    @Test
    fun commitText_withCustomCursor_delegatesToConnection() {
        bridge.commitText("hello", 2)
        verify(mockConnection).commitText("hello", 2)
    }

    @Test
    fun setComposingText_delegatesToConnection() {
        bridge.setComposingText("hello")
        verify(mockConnection).setComposingText("hello", 1)
    }

    @Test
    fun deleteSurroundingText_delegatesToConnection() {
        bridge.deleteSurroundingText(1, 0)
        verify(mockConnection).deleteSurroundingText(1, 0)
    }

    @Test
    fun deleteSurroundingText_withAfter_delegatesToConnection() {
        bridge.deleteSurroundingText(1, 1)
        verify(mockConnection).deleteSurroundingText(1, 1)
    }

    @Test
    fun getTextBeforeCursor_delegatesToConnection() {
        `when`(mockConnection.getTextBeforeCursor(100, 0)).thenReturn("test")
        val result = bridge.getTextBeforeCursor()
        assertEquals("test", result)
    }

    @Test
    fun getTextAfterCursor_delegatesToConnection() {
        `when`(mockConnection.getTextAfterCursor(100, 0)).thenReturn("test")
        val result = bridge.getTextAfterCursor()
        assertEquals("test", result)
    }

    @Test
    fun finishComposing_delegatesToConnection() {
        bridge.finishComposing()
        verify(mockConnection).finishComposingText()
    }

    @Test
    fun commitText_nullConnection_doesNotCrash() {
        bridge.currentInputConnection = null
        bridge.commitText("hello")
        // No crash = pass
        assertTrue(true)
    }

    @Test
    fun deleteSurroundingText_nullConnection_doesNotCrash() {
        bridge.currentInputConnection = null
        bridge.deleteSurroundingText(1, 0)
        // No crash = pass
        assertTrue(true)
    }
}
