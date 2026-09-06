package com.justtype.shellkeyboard.keyboard.ui

import com.justtype.shellkeyboard.keyboard.ui.KeyboardWindow.KeyboardType
import org.junit.Assert.*
import org.junit.Test

class KeyboardWindowTest {

    @Test
    fun switchTo_changesType() {
        val window = KeyboardWindow(mock(android.content.Context::class.java))
        assertEquals(KeyboardType.QWERTY, window.getCurrentType())
        
        window.switchTo(KeyboardType.SYMBOLS)
        assertEquals(KeyboardType.SYMBOLS, window.getCurrentType())
        
        window.switchTo(KeyboardType.NUMPAD)
        assertEquals(KeyboardType.NUMPAD, window.getCurrentType())
        
        window.switchTo(KeyboardType.PROGRAMMER)
        assertEquals(KeyboardType.PROGRAMMER, window.getCurrentType())
    }

    @Test
    fun switchTo_sameType_noChange() {
        val window = KeyboardWindow(mock(android.content.Context::class.java))
        assertEquals(KeyboardType.QWERTY, window.getCurrentType())
        window.switchTo(KeyboardType.QWERTY)
        assertEquals(KeyboardType.QWERTY, window.getCurrentType())
    }

    @Test
    fun keyboardType_values() {
        val types = KeyboardType.values()
        assertEquals(4, types.size)
        assertTrue(types.contains(KeyboardType.QWERTY))
        assertTrue(types.contains(KeyboardType.SYMBOLS))
        assertTrue(types.contains(KeyboardType.NUMPAD))
        assertTrue(types.contains(KeyboardType.PROGRAMMER))
    }
}
