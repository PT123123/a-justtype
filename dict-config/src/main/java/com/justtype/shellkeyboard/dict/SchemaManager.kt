package com.justtype.shellkeyboard.dict

import com.justtype.shellkeyboard.core.Rime
import com.justtype.shellkeyboard.core.RimeSession

class SchemaManager {

    private var currentSchemaId: String = ""
    private var availableSchemas: List<Rime.SchemaInfo> = emptyList()

    fun loadSchemas() {
        availableSchemas = Rime.rimeGetSchemaList() ?: emptyList()
        if (availableSchemas.isNotEmpty() && currentSchemaId.isEmpty()) {
            currentSchemaId = availableSchemas.first().schemaId
        }
    }

    fun getAvailableSchemas(): List<Rime.SchemaInfo> = availableSchemas

    fun switchSchema(schemaId: String, session: RimeSession?): Boolean {
        if (session == null) return false
        val success = session.setSchema(schemaId)
        if (success) {
            currentSchemaId = schemaId
        }
        return success
    }

    fun getCurrentSchemaId(): String = currentSchemaId

    fun getSchemaName(schemaId: String): String {
        return availableSchemas.find { it.schemaId == schemaId }?.name ?: schemaId
    }

    fun cycleSchema(session: RimeSession?): String? {
        if (availableSchemas.isEmpty()) return null
        val currentIndex = availableSchemas.indexOfFirst { it.schemaId == currentSchemaId }
        val nextIndex = (currentIndex + 1) % availableSchemas.size
        val nextSchema = availableSchemas[nextIndex]
        switchSchema(nextSchema.schemaId, session)
        return nextSchema.schemaId
    }
}
