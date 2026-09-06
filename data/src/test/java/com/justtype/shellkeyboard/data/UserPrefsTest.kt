package com.justtype.shellkeyboard.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UserPrefsTest {

    private lateinit var userPrefs: UserPrefs

    @Before
    fun setUp() {
        userPrefs = UserPrefs(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun theme_defaultIsSystem() = runBlocking {
        val theme = userPrefs.theme.first()
        assertEquals("system", theme)
    }

    @Test
    fun setTheme_updatesValue() = runBlocking {
        userPrefs.setTheme("dark")
        val theme = userPrefs.theme.first()
        assertEquals("dark", theme)
    }

    @Test
    fun keyboardHeight_defaultIs80() = runBlocking {
        val height = userPrefs.keyboardHeight.first()
        assertEquals(80, height)
    }

    @Test
    fun setKeyboardHeight_updatesValue() = runBlocking {
        userPrefs.setKeyboardHeight(100)
        val height = userPrefs.keyboardHeight.first()
        assertEquals(100, height)
    }

    @Test
    fun hapticFeedback_defaultIsTrue() = runBlocking {
        val enabled = userPrefs.hapticFeedback.first()
        assertTrue(enabled)
    }

    @Test
    fun setHapticFeedback_updatesValue() = runBlocking {
        userPrefs.setHapticFeedback(false)
        val enabled = userPrefs.hapticFeedback.first()
        assertFalse(enabled)
    }

    @Test
    fun soundFeedback_defaultIsTrue() = runBlocking {
        val enabled = userPrefs.soundFeedback.first()
        assertTrue(enabled)
    }

    @Test
    fun setSoundFeedback_updatesValue() = runBlocking {
        userPrefs.setSoundFeedback(false)
        val enabled = userPrefs.soundFeedback.first()
        assertFalse(enabled)
    }

    @Test
    fun currentSchema_defaultIsLunaPinyin() = runBlocking {
        val schema = userPrefs.currentSchema.first()
        assertEquals("luna_pinyin", schema)
    }

    @Test
    fun setCurrentSchema_updatesValue() = runBlocking {
        userPrefs.setCurrentSchema("wubi86")
        val schema = userPrefs.currentSchema.first()
        assertEquals("wubi86", schema)
    }

    @Test
    fun cloudSync_defaultIsFalse() = runBlocking {
        val enabled = userPrefs.cloudSync.first()
        assertFalse(enabled)
    }

    @Test
    fun setCloudSync_updatesValue() = runBlocking {
        userPrefs.setCloudSync(true)
        val enabled = userPrefs.cloudSync.first()
        assertTrue(enabled)
    }
}
