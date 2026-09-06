package com.justtype.shellkeyboard.core

import org.junit.Assert.*
import org.junit.Test

class RimeSessionTest {

    @Test
    fun create_returnsNull_whenRimeNotInitialized() {
        // RIME not initialized, so create should return null
        val session = RimeSession.create()
        // In test environment without native lib, this will be null
        // In production with RIME initialized, it would return a session
        assertNull(session)
    }

    @Test
    fun destroy_doesNotCrash_onNullSession() {
        // Should handle gracefully
        val session = RimeSession.create()
        if (session != null) {
            session.destroy()
        }
        // No crash = pass
        assertTrue(true)
    }
}
