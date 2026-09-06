package com.justtype.shellkeyboard.core

import org.junit.Assert.*
import org.junit.Test

class RimeLifecycleTest {

    @Test
    fun values_areCorrect() {
        val values = RimeLifecycle.values()
        assertEquals(4, values.size)
        assertTrue(values.contains(RimeLifecycle.STOPPED))
        assertTrue(values.contains(RimeLifecycle.STARTING))
        assertTrue(values.contains(RimeLifecycle.READY))
        assertTrue(values.contains(RimeLifecycle.STOPPING))
    }

    @Test
    fun valueOf_works() {
        assertEquals(RimeLifecycle.STOPPED, RimeLifecycle.valueOf("STOPPED"))
        assertEquals(RimeLifecycle.STARTING, RimeLifecycle.valueOf("STARTING"))
        assertEquals(RimeLifecycle.READY, RimeLifecycle.valueOf("READY"))
        assertEquals(RimeLifecycle.STOPPING, RimeLifecycle.valueOf("STOPPING"))
    }

    @Test
    fun lifecycle_transitionsCorrectly() {
        var state: RimeLifecycle = RimeLifecycle.STOPPED
        assertEquals(RimeLifecycle.STOPPED, state)
        
        state = RimeLifecycle.STARTING
        assertEquals(RimeLifecycle.STARTING, state)
        
        state = RimeLifecycle.READY
        assertEquals(RimeLifecycle.READY, state)
        
        state = RimeLifecycle.STOPPING
        assertEquals(RimeLifecycle.STOPPING, state)
        
        state = RimeLifecycle.STOPPED
        assertEquals(RimeLifecycle.STOPPED, state)
    }
}
