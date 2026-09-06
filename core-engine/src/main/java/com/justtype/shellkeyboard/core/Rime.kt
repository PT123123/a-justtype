package com.justtype.shellkeyboard.core

/**
 * JNI wrapper for librime native engine.
 * 
 * All native calls must go through RimeDispatcher for thread safety.
 */
object Rime {

    init {
        System.loadLibrary("rime")
    }

    // Native methods (JNI bindings to librime)
    external fun rimeInitialize(configDir: String, userDir: String): Boolean
    external fun rimeFinalize()
    external fun rimeProcessKey(sessionId: Long, keyCode: Int, modifiers: Int): Boolean
    external fun rimeGetCommit(sessionId: Long): String?
    external fun rimeGetContext(sessionId: Long): RimeContext?
    external fun rimeGetStatus(sessionId: Long): RimeStatus?
    external fun rimeSetOption(sessionId: Long, option: String, value: Boolean)
    external fun rimeGetOption(sessionId: Long, option: String): Boolean
    external fun rimeGetCurrentSchema(sessionId: Long): String?
    external fun rimeSetSchema(sessionId: Long, schemaId: String): Boolean
    external fun rimeGetSchemaList(): List<SchemaInfo>
    external fun rimeCreateSession(): Long
    external fun rimeDestroySession(sessionId: Long)
    external fun rimeSimulateKeySequence(sessionId: Long, sequence: String): Boolean
    external fun rimeCommit(): String?
    external fun rimeCleanup()

    /**
     * Data class for RIME context (candidates, preedit, etc.)
     */
    data class RimeContext(
        val preedit: String,
        val composition: Composition,
        val menu: Menu,
        val commitText: String
    )

    data class Composition(
        val length: Int,
        val cursorPos: Int,
        val selStart: Int,
        val selEnd: Int,
        val preedit: String
    )

    data class Menu(
        val pageSize: Int,
        val pageNo: Int,
        val isLastPage: Boolean,
        val highlightedCandidateIndex: Int,
        val numCandidates: Int,
        val candidates: List<Candidate>
    )

    data class Candidate(
        val text: String,
        val comment: String
    )

    data class RimeStatus(
        val schemaId: String,
        val isDisabled: Boolean,
        val isComposing: Boolean,
        val isAsciiMode: Boolean,
        val isFullShape: Boolean,
        val isSimplified: Boolean,
        val isTraditional: Boolean,
        val isAsciiPunct: Boolean
    )

    data class SchemaInfo(
        val schemaId: String,
        val name: String,
        val version: String,
        val author: String
    )
}
