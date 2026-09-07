package com.justtype.shellkeyboard.keyboard.ui

import android.graphics.PointF
import kotlin.math.sqrt

class SwipeInput {

    private val points = mutableListOf<PointF>()

    fun startSwipe(x: Float, y: Float) {
        points.clear()
        points.add(PointF(x, y))
    }

    fun addPoint(x: Float, y: Float) {
        points.add(PointF(x, y))
    }

    fun endSwipe(): String? {
        if (points.size < 2) return null
        val totalDistance = calculateTotalDistance()
        if (totalDistance < 100) return null
        return matchWord(getKeySequence())
    }

    fun clear() {
        points.clear()
    }

    private fun calculateTotalDistance(): Float {
        var distance = 0f
        for (i in 1 until points.size) {
            val dx = points[i].x - points[i-1].x
            val dy = points[i].y - points[i-1].y
            distance += sqrt(dx * dx + dy * dy)
        }
        return distance
    }

    private fun getKeySequence(): List<String> {
        if (points.size < 2) return emptyList()
        val keys = mutableListOf<String>()
        val qwertyRows = listOf(
            listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
            listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
            listOf("z", "x", "c", "v", "b", "n", "m")
        )
        val keyWidth = 100f
        val rowHeight = 100f
        for (point in points) {
            val row = (point.y / rowHeight).toInt().coerceIn(0, qwertyRows.size - 1)
            val col = (point.x / keyWidth).toInt().coerceIn(0, qwertyRows[row].size - 1)
            keys.add(qwertyRows[row][col])
        }
        return keys.distinct()
    }

    private fun matchWord(keySequence: List<String>): String? {
        if (keySequence.isEmpty()) return null
        val word = keySequence.joinToString("")
        val commonWords = listOf("the", "and", "for", "are", "but", "not", "you", "all", "can", "had", "her", "was", "one", "our", "out", "day", "get", "has", "him", "his", "how", "its", "may", "new", "now", "old", "see", "two", "way", "who", "boy", "did", "own", "say", "she", "too", "use", "word", "work", "with", "this", "that", "from", "they", "been", "have", "some", "what", "when", "make", "like", "just", "take", "come", "could", "would", "should", "there", "their", "where", "which", "about")
        return commonWords.find { it.startsWith(word) && it.length <= word.length + 2 }
    }
}
