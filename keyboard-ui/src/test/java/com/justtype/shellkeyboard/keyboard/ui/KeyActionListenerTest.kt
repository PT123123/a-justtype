package com.justtype.shellkeyboard.keyboard.ui

import android.view.KeyEvent
import com.justtype.shellkeyboard.core.RimeDispatcher
import com.justtype.shellkeyboard.ime.InputConnectionBridge
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class KeyActionListenerTest {

    private lateinit var mockDispatcher: RimeDispatcher
    private lateinit var mockBridge: InputConnectionBridge
    private lateinit var listener: KeyActionListener

    @Before
    fun setUp() {
        mockDispatcher = mock(RimeDispatcher::class.java)
        mockBridge = mock(InputConnectionBridge::class.java)
        listener = KeyActionListener(mockDispatcher, mockBridge)
    }

    @Test
    fun onKeyPress_backspace_callsDelete() {
        listener.onKeyPress(KeyEvent.KEYCODE_DEL)
        verify(mockBridge).deleteSurroundingText(1, 0)
    }

    @Test
    fun onKeyPress_enter_callsSendEnter() {
        listener.onKeyPress(KeyEvent.KEYCODE_ENTER)
        verify(mockBridge).sendEnter()
    }

    @Test
    fun onKeyPress_space_callsCommitSpace() {
        listener.onKeyPress(KeyEvent.KEYCODE_SPACE)
        verify(mockBridge).commitText(" ")
    }

    @Test
    fun onKeyPress_letter_dispatchesToRime() {
        listener.onKeyPress(KeyEvent.KEYCODE_A)
        verify(mockDispatcher).execute(any())
    }

    @Test
    fun onKeyPress_letterZ_dispatchesToRime() {
        listener.onKeyPress(KeyEvent.KEYCODE_Z)
        verify(mockDispatcher).execute(any())
    }

    @Test
    fun onKeyPress_symbol_commitsDirectly() {
        listener.onKeyPress(KeyEvent.KEYCODE_PERIOD)
        verify(mockBridge).commitText(any())
    }
}
