package com.justtype.shellkeyboard.keyboard.ui

import android.graphics.PointF
import kotlin.math.sqrt

/**
 * Swipe/swype input detection.
 */
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
        return emptyList()
    }

    private fun matchWord(keySequence: List<String>): String? {
        return null
    }
}
