package com.justtype.shellkeyboard.ime

import com.justtype.shellkeyboard.core.RimeDispatcher
import com.justtype.shellkeyboard.core.RimeLifecycle

/**
 * Manages the RIME engine lifecycle.
 * 
 * States: STOPPED → STARTING → READY → STOPPING → STOPPED
 */
class LifecycleManager(private val dispatcher: RimeDispatcher) {

    private var state: RimeLifecycle = RimeLifecycle.STOPPED

    fun initialize() {
        if (state != RimeLifecycle.STOPPED) return
        state = RimeLifecycle.STARTING
        dispatcher.execute {
            // Initialize RIME engine, deploy schemas
            state = RimeLifecycle.READY
        }
    }

    fun destroy() {
        state = RimeLifecycle.STOPPING
        dispatcher.execute {
            // Cleanup RIME engine
            state = RimeLifecycle.STOPPED
        }
    }

    fun isReady(): Boolean = state == RimeLifecycle.READY
}
