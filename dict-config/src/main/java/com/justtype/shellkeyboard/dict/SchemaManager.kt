package com.justtype.shellkeyboard.dict

import com.justtype.shellkeyboard.core.Rime
import com.justtype.shellkeyboard.core.RimeSession

/**
 * Manages RIME input schemas (拼音, 五笔, 双拼, etc.)
 */
class SchemaManager {

    private var currentSchemaId: String = ""
    private var availableSchemas: List<Rime.SchemaInfo> = emptyList()

    /**
     * Load available schemas from RIME.
     */
    fun loadSchemas() {
        availableSchemas = Rime.rimeGetSchemaList() ?: emptyList()
    }

    /**
     * Get list of available schemas.
     */
    fun getAvailableSchemas(): List<Rime.SchemaInfo> = availableSchemas

    /**
     * Switch to a different schema.
     */
    fun switchSchema(schemaId: String, session: RimeSession?): Boolean {
        if (session == null) return false
        val success = session.setSchema(schemaId)
        if (success) {
            currentSchemaId = schemaId
        }
        return success
    }

    /**
     * Get current schema ID.
     */
    fun getCurrentSchemaId(): String = currentSchemaId

    /**
     * Get schema display name.
     */
    fun getSchemaName(schemaId: String): String {
        return availableSchemas.find { it.schemaId == schemaId }?.name ?: schemaId
    }

    /**
     * Cycle through available schemas.
     */
    fun cycleSchema(session: RimeSession?): String? {
        if (availableSchemas.isEmpty()) return null
        val currentIndex = availableSchemas.indexOfFirst { it.schemaId == currentSchemaId }
        val nextIndex = (currentIndex + 1) % availableSchemas.size
        val nextSchema = availableSchemas[nextIndex]
        switchSchema(nextSchema.schemaId, session)
        return nextSchema.schemaId
    }
}
