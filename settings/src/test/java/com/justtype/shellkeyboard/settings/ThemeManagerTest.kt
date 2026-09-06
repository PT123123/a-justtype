package com.justtype.shellkeyboard.settings

import androidx.appcompat.app.AppCompatDelegate
import org.junit.Assert.*
import org.junit.Test
import com.justtype.shellkeyboard.settings.ThemeManager.Theme

class ThemeManagerTest {

    @Test
    fun theme_values() {
        val themes = Theme.values()
        assertEquals(3, themes.size)
        assertTrue(themes.contains(Theme.LIGHT))
        assertTrue(themes.contains(Theme.DARK))
        assertTrue(themes.contains(Theme.SYSTEM))
    }

    @Test
    fun applyTheme_light_setsNightModeNo() {
        val mockContext = mock(android.content.Context::class.java)
        val manager = ThemeManager(mockContext)
        manager.applyTheme(Theme.LIGHT)
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, AppCompatDelegate.getDefaultNightMode())
    }

    @Test
    fun applyTheme_dark_setsNightModeYes() {
        val mockContext = mock(android.content.Context::class.java)
        val manager = ThemeManager(mockContext)
        manager.applyTheme(Theme.DARK)
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, AppCompatDelegate.getDefaultNightMode())
    }

    @Test
    fun applyTheme_system_setsNightModeFollowSystem() {
        val mockContext = mock(android.content.Context::class.java)
        val manager = ThemeManager(mockContext)
        manager.applyTheme(Theme.SYSTEM)
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, AppCompatDelegate.getDefaultNightMode())
    }

    @Test
    fun getCurrentTheme_returnsSystem() {
        val mockContext = mock(android.content.Context::class.java)
        val manager = ThemeManager(mockContext)
        assertEquals(Theme.SYSTEM, manager.getCurrentTheme())
    }
}
