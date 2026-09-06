package com.justtype.shellkeyboard.core

/**
 * Represents a single RIME input session.
 * 
 * Each input field gets its own session to maintain independent state.
 */
class RimeSession private constructor(val sessionId: Long) {

    companion object {
        fun create(): RimeSession? {
            val id = Rime.rimeCreateSession()
            return if (id != 0L) RimeSession(id) else null
        }
    }

    fun processKey(keyCode: Int, modifiers: Int = 0): Boolean {
        return Rime.rimeProcessKey(sessionId, keyCode, modifiers)
    }

    fun getCommit(): String? {
        return Rime.rimeGetCommit(sessionId)
    }

    fun getContext(): Rime.RimeContext? {
        return Rime.rimeGetContext(sessionId)
    }

    fun getStatus(): Rime.RimeStatus? {
        return Rime.rimeGetStatus(sessionId)
    }

    fun setOption(option: String, value: Boolean) {
        Rime.rimeSetOption(sessionId, option, value)
    }

    fun getOption(option: String): Boolean {
        return Rime.rimeGetOption(sessionId, option)
    }

    fun getCurrentSchema(): String? {
        return Rime.rimeGetCurrentSchema(sessionId)
    }

    fun setSchema(schemaId: String): Boolean {
        return Rime.rimeSetSchema(sessionId, schemaId)
    }

    fun simulateKeySequence(sequence: String): Boolean {
        return Rime.rimeSimulateKeySequence(sessionId, sequence)
    }

    fun destroy() {
        Rime.rimeDestroySession(sessionId)
    }
}
