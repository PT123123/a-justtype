package com.justtype.shellkeyboard.settings

import android.content.Context
import android.util.DisplayMetrics
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class KeyboardHeightManagerTest {

    @Test
    fun minHeightRatio_isFiftyPercent() {
        assertEquals(0.5f, KeyboardHeightManager.MIN_HEIGHT_RATIO, 0.01f)
    }

    @Test
    fun maxHeightRatio_isHundredPercent() {
        assertEquals(1.0f, KeyboardHeightManager.MAX_HEIGHT_RATIO, 0.01f)
    }

    @Test
    fun defaultHeightRatio_isEightyPercent() {
        assertEquals(0.8f, KeyboardHeightManager.DEFAULT_HEIGHT_RATIO, 0.01f)
    }

    @Test
    fun getHeightRatio_returnsDefault() {
        val mockContext = mock(Context::class.java)
        val mockResources = mock(android.content.res.Resources::class.java)
        val mockMetrics = DisplayMetrics().apply { heightPixels = 2000 }
        `when`(mockContext.resources).thenReturn(mockResources)
        `when`(mockResources.displayMetrics).thenReturn(mockMetrics)
        
        val manager = KeyboardHeightManager(mockContext)
        assertEquals(0.8f, manager.getHeightRatio(), 0.01f)
    }

    @Test
    fun getKeyboardHeight_returnsCorrectValue() {
        val mockContext = mock(Context::class.java)
        val mockResources = mock(android.content.res.Resources::class.java)
        val mockMetrics = DisplayMetrics().apply { heightPixels = 2000 }
        `when`(mockContext.resources).thenReturn(mockResources)
        `when`(mockResources.displayMetrics).thenReturn(mockMetrics)
        
        val manager = KeyboardHeightManager(mockContext)
        // 2000 * 0.8 = 1600
        assertEquals(1600, manager.getKeyboardHeight())
    }
}
