package com.justtype.shellkeyboard.keyboard.ui

import com.justtype.shellkeyboard.keyboard.ui.InputConnectionBridge

/**
 * Auto bracket pairing with cursor centering.
 */
class BracketPair(private val bridge: InputConnectionBridge) {

    private val pairs = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
        '"' to '"',
        '`' to '`'
    )

    private val closingBrackets = setOf(')', ']', '}', '>', '"', '`')

    fun process(char: String): Boolean {
        if (char.length != 1) return false
        val c = char[0]
        if (pairs.containsKey(c)) {
            val closing = pairs[c]
            bridge.commitText(c.toString() + closing)
            bridge.moveCursor(-1)
            return true
        }
        if (closingBrackets.contains(c)) {
            val nextChar = bridge.getTextAfterCursor(1)
            if (nextChar?.toString() == c.toString()) {
                bridge.moveCursor(1)
                return true
            }
        }
        return false
    }

    fun isBracket(char: String): Boolean {
        if (char.length != 1) return false
        val c = char[0]
        return pairs.containsKey(c) || closingBrackets.contains(c)
    }
}
