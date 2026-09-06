package com.justtype.shellkeyboard.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/**
 * Manages keyboard themes.
 * 
 * Supports:
 * - Light theme
 * - Dark theme
 * - System default
 * - Custom themes (future)
 */
class ThemeManager(private val context: Context) {

    enum class Theme {
        LIGHT,
        DARK,
        SYSTEM
    }

    fun applyTheme(theme: Theme) {
        val mode = when (theme) {
            Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun getCurrentTheme(): Theme {
        return Theme.SYSTEM // TODO: Load from preferences
    }
}
