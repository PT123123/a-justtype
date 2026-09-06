package com.justtype.shellkeyboard.core

import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Serializes all RIME native calls onto a single thread.
 * 
 * CRITICAL: librime is NOT thread-safe. All native calls MUST go through
 * this dispatcher to avoid race conditions and crashes.
 * 
 * This is the same pattern used by Trime (同文输入法).
 */
class RimeDispatcher {

    private val executor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "rime-main").apply { isDaemon = true }
    }
    private val isShutdown = AtomicBoolean(false)

    /**
     * Execute a RIME operation on the rime-main thread.
     */
    fun execute(operation: () -> Unit) {
        if (isShutdown.get()) return
        executor.execute(operation)
    }

    /**
     * Execute a RIME operation and wait for result.
     */
    fun <T> executeAndWait(operation: () -> T): T {
        if (isShutdown.get()) throw IllegalStateException("Dispatcher is shut down")
        val future = executor.submit(operation)
        return future.get()
    }

    /**
     * Shutdown the dispatcher.
     */
    fun shutdown() {
        isShutdown.set(true)
        executor.shutdown()
    }
}
