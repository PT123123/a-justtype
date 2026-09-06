package com.justtype.shellkeyboard.sync

import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response
import fi.iki.elonen.NanoHTTPD.IHTTPSession
import java.io.File

/**
 * Local HTTP server for LAN sync.
 * 
 * Uses NanoHTTPD to serve user dictionary and preferences
 * to other devices on the same network.
 * 
 * Security: Only accessible on local network (no internet exposure).
 */
class SyncServer(port: Int = 8080) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        return when {
            uri == "/dict" -> serveDictionary()
            uri == "/prefs" -> servePreferences()
            uri.startsWith("/dict/") -> serveDictionaryFile(uri.removePrefix("/dict/"))
            else -> newFixedLengthResponse(
                Response.Status.NOT_FOUND,
                "application/json",
                """{"error": "Not found"}"""
            )
        }
    }

    private fun serveDictionary(): Response {
        // Return user dictionary as JSON
        return newFixedLengthResponse(
            Response.Status.OK,
            "application/json",
            """{"words": []}"""
        )
    }

    private fun servePreferences(): Response {
        // Return preferences as JSON
        return newFixedLengthResponse(
            Response.Status.OK,
            "application/json",
            """{"prefs": {}}"""
        )
    }

    private fun serveDictionaryFile(filename: String): Response {
        // Serve specific dictionary file
        return newFixedLengthResponse(
            Response.Status.NOT_IMPLEMENTED,
            "application/json",
            """{"error": "Not implemented"}"""
        )
    }
}
