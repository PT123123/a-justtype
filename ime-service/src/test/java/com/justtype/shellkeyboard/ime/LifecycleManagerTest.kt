package com.justtype.shellkeyboard.ime

import com.justtype.shellkeyboard.core.RimeDispatcher
import com.justtype.shellkeyboard.core.RimeLifecycle
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class LifecycleManagerTest {

    private lateinit var mockDispatcher: RimeDispatcher
    private lateinit var lifecycleManager: LifecycleManager

    @Before
    fun setUp() {
        mockDispatcher = mock(RimeDispatcher::class.java)
        lifecycleManager = LifecycleManager(mockDispatcher)
    }

    @Test
    fun initialize_dispatchesOperation() {
        lifecycleManager.initialize()
        verify(mockDispatcher).execute(any())
    }

    @Test
    fun destroy_dispatchesOperation() {
        lifecycleManager.destroy()
        verify(mockDispatcher).execute(any())
    }

    @Test
    fun isReady_returnsFalse_initially() {
        assertFalse(lifecycleManager.isReady())
    }

    @Test
    fun initialize_onlyOnce_whenCalledMultipleTimes() {
        lifecycleManager.initialize()
        lifecycleManager.initialize()
        // execute should only be called once
        verify(mockDispatcher, times(1)).execute(any())
    }
}
