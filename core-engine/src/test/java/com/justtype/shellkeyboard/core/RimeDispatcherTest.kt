package com.justtype.shellkeyboard.core

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class RimeDispatcherTest {

    private lateinit var dispatcher: RimeDispatcher

    @Before
    fun setUp() {
        dispatcher = RimeDispatcher()
    }

    @After
    fun tearDown() {
        dispatcher.shutdown()
    }

    @Test
    fun execute_runsOperation() {
        val counter = AtomicInteger(0)
        dispatcher.execute { counter.incrementAndGet() }
        Thread.sleep(100)
        assertEquals(1, counter.get())
    }

    @Test
    fun executeAndWait_returnsResult() {
        val result = dispatcher.executeAndWait { 42 }
        assertEquals(42, result)
    }

    @Test
    fun executeAndWait_serializesOperations() {
        val order = mutableListOf<Int>()
        val t1 = Thread {
            dispatcher.executeAndWait { Thread.sleep(50); order.add(1) }
        }
        val t2 = Thread {
            dispatcher.executeAndWait { order.add(2) }
        }
        t1.start()
        t2.start()
        t1.join()
        t2.join()
        assertEquals(listOf(1, 2), order)
    }

    @Test
    fun execute_afterShutdown_doesNothing() {
        dispatcher.shutdown()
        val counter = AtomicInteger(0)
        dispatcher.execute { counter.incrementAndGet() }
        Thread.sleep(100)
        assertEquals(0, counter.get())
    }

    @Test
    fun executeAndWait_afterShutdown_throws() {
        dispatcher.shutdown()
        try {
            dispatcher.executeAndWait { 1 }
            fail("Expected IllegalStateException")
        } catch (e: IllegalStateException) {
            assertTrue(e.message!!.contains("shut down"))
        }
    }
}
