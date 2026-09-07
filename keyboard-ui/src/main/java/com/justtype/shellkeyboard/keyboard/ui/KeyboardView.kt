package com.justtype.shellkeyboard.keyboard.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.justtype.shellkeyboard.core.RimeDispatcher
import com.justtype.shellkeyboard.keyboard.ui.InputConnectionBridge
import com.justtype.shellkeyboard.keyboard.ui.KeyboardWindow.KeyboardType

class KeyboardView(
    private val context: Context,
    private val rimeDispatcher: RimeDispatcher,
    private val inputConnectionBridge: InputConnectionBridge
) {
    val view: View
    private val keyActionListener = KeyActionListener(rimeDispatcher, inputConnectionBridge)
    private var isPasswordMode = false
    private var currentKeyboardType = KeyboardType.QWERTY
    private val keyMargin = 4f
    private val keyCornerRadius = 8f
    private val keyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFE8E8E8.toInt(); style = Paint.Style.FILL }
    private val keyTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF333333.toInt(); textSize = 24f; textAlign = Paint.Align.CENTER; typeface = Typeface.DEFAULT }
    private val functionKeyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFBDBDBD.toInt(); style = Paint.Style.FILL }
    private val functionKeyTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF333333.toInt(); textSize = 18f; textAlign = Paint.Align.CENTER; typeface = Typeface.DEFAULT_BOLD }
    private val pressedKeyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF6750A4.toInt(); style = Paint.Style.FILL }
    private val pressedKeyTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE; textSize = 24f; textAlign = Paint.Align.CENTER }
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0x1A000000; style = Paint.Style.FILL }
    private val popupPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF333333.toInt(); style = Paint.Style.FILL }
    private val popupTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE; textSize = 36f; textAlign = Paint.Align.CENTER }
    private val keys = mutableListOf<KeyInfo>()
    private val pressedKeys = mutableSetOf<Int>()
    private val keyRects = mutableMapOf<Int, RectF>()
    private var popupKey: KeyInfo? = null
    private var popupRect: RectF? = null

    data class KeyInfo(val label: String, val code: Int, val type: KeyType, val width: Float = 1f)
    enum class KeyType { CHARACTER, FUNCTION, MODIFIER }

    private val qwertyRows = listOf(
        listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
        listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
        listOf("⇧", "z", "x", "c", "v", "b", "n", "m", "⌫")
    )
    private val numberRow = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

    init {
        view = object : View(context) {
            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                drawKeyboard(canvas)
                drawPopup(canvas)
            }
            override fun onTouchEvent(event: MotionEvent): Boolean {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> handleTouchDown(event.x, event.y)
                    MotionEvent.ACTION_UP -> handleTouchUp(event.x, event.y)
                    MotionEvent.ACTION_MOVE -> handleTouchMove(event.x, event.y)
                    MotionEvent.ACTION_CANCEL -> { pressedKeys.clear(); popupKey = null; invalidate() }
                }
                return true
            }
        }
        buildKeys()
    }

    private fun buildKeys() {
        keys.clear()
        when (currentKeyboardType) {
            KeyboardType.QWERTY -> buildQwertyKeys()
            KeyboardType.SYMBOLS -> buildSymbolKeys()
            KeyboardType.NUMPAD -> buildNumpadKeys()
            KeyboardType.PROGRAMMER -> buildProgrammerKeys()
        }
    }

    private fun buildQwertyKeys() {
        numberRow.forEach { keys.add(KeyInfo(it, it.first().code, KeyType.CHARACTER)) }
        qwertyRows.forEach { row ->
            row.forEach { key ->
                val code = when (key) { "⇧" -> KeyEvent.KEYCODE_SHIFT_LEFT; "⌫" -> KeyEvent.KEYCODE_DEL; else -> key.first().code }
                val type = if (key == "⇧" || key == "⌫") KeyType.FUNCTION else KeyType.CHARACTER
                keys.add(KeyInfo(key, code, type))
            }
        }
        keys.add(KeyInfo("?123", -1, KeyType.FUNCTION, 1.5f))
        keys.add(KeyInfo("🌐", -2, KeyType.FUNCTION, 1f))
        keys.add(KeyInfo("space", KeyEvent.KEYCODE_SPACE, KeyType.FUNCTION, 4f))
        keys.add(KeyInfo(".", KeyEvent.KEYCODE_PERIOD, KeyType.CHARACTER))
        keys.add(KeyInfo("↵", KeyEvent.KEYCODE_ENTER, KeyType.FUNCTION, 1.5f))
    }

    private fun buildSymbolKeys() {
        listOf("1","2","3","4","5","6","7","8","9","0","!","@","#","$","%","^","&","*","(",")","{","}","[","]","<",">","=","+","-","_").forEach {
            keys.add(KeyInfo(it, it.first().code, KeyType.CHARACTER))
        }
        keys.add(KeyInfo("ABC", -1, KeyType.FUNCTION, 1.5f))
        keys.add(KeyInfo("space", KeyEvent.KEYCODE_SPACE, KeyType.FUNCTION, 5f))
        keys.add(KeyInfo("↵", KeyEvent.KEYCODE_ENTER, KeyType.FUNCTION, 1.5f))
    }

    private fun buildNumpadKeys() {
        listOf("7","8","9","4","5","6","1","2","3","0").forEach { keys.add(KeyInfo(it, it.first().code, KeyType.CHARACTER)) }
        keys.add(KeyInfo(".", KeyEvent.KEYCODE_PERIOD, KeyType.CHARACTER))
        keys.add(KeyInfo("⌫", KeyEvent.KEYCODE_DEL, KeyType.FUNCTION))
    }

    private fun buildProgrammerKeys() {
        listOf("{","}","[","]","(",")","<",">","=","!","&","|","+","-","*","/",";","\"","'","\\","~","`","#").forEach {
            keys.add(KeyInfo(it, it.first().code, KeyType.CHARACTER))
        }
        keys.add(KeyInfo("ABC", -1, KeyType.FUNCTION, 1.5f))
        keys.add(KeyInfo("space", KeyEvent.KEYCODE_SPACE, KeyType.FUNCTION, 5f))
        keys.add(KeyInfo("↵", KeyEvent.KEYCODE_ENTER, KeyType.FUNCTION, 1.5f))
    }

    private fun drawKeyboard(canvas: Canvas) {
        keyRects.clear()
        val width = view.width.toFloat()
        val totalRows = 5
        val rowHeight = view.height / totalRows
        var y = 0f
        for (rowNum in 0 until totalRows) {
            val keysInRow = getKeysInRow(rowNum)
            val totalWeight = keysInRow.sumOf { it.width.toDouble() }.toFloat()
            val keyWidth = width / totalWeight
            var x = 0f
            for (key in keysInRow) {
                val rect = RectF(x + keyMargin, y + keyMargin, x + keyWidth * key.width - keyMargin, y + rowHeight - keyMargin)
                canvas.drawRoundRect(RectF(rect.left + 2, rect.top + 2, rect.right + 2, rect.bottom + 2), keyCornerRadius, keyCornerRadius, shadowPaint)
                val isPressed = pressedKeys.contains(key.code)
                val paint = when { isPressed -> pressedKeyPaint; key.type == KeyType.FUNCTION -> functionKeyPaint; else -> keyPaint }
                val textPaint = when { isPressed -> pressedKeyTextPaint; key.type == KeyType.FUNCTION -> functionKeyTextPaint; else -> keyTextPaint }
                canvas.drawRoundRect(rect, keyCornerRadius, keyCornerRadius, paint)
                val textBounds = android.graphics.Rect()
                textPaint.getTextBounds(key.label, 0, key.label.length, textBounds)
                canvas.drawText(key.label, rect.centerX(), rect.centerY() + textBounds.height() / 2f - textBounds.bottom, textPaint)
                keyRects[key.code] = rect
                x += keyWidth * key.width
            }
            y += rowHeight
        }
    }

    private fun drawPopup(canvas: Canvas) {
        val popup = popupKey ?: return
        val rect = popupRect ?: return
        canvas.drawRoundRect(rect, keyCornerRadius * 2, keyCornerRadius * 2, popupPaint)
        val textBounds = android.graphics.Rect()
        popupTextPaint.getTextBounds(popup.label, 0, popup.label.length, textBounds)
        canvas.drawText(popup.label, rect.centerX(), rect.centerY() + textBounds.height() / 2f, popupTextPaint)
    }

    private fun getKeysInRow(row: Int): List<KeyInfo> {
        return when (row) {
            0 -> keys.filter { it.label.length == 1 && it.label[0].isDigit() }
            1 -> keys.filter { it.label in listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p") }
            2 -> keys.filter { it.label in listOf("a", "s", "d", "f", "g", "h", "j", "k", "l") }
            3 -> keys.filter { it.label in listOf("⇧", "z", "x", "c", "v", "b", "n", "m", "⌫") }
            else -> keys.filter { it.label in listOf("?123", "🌐", "space", ".", "↵") }
        }
    }

    private fun handleTouchDown(x: Float, y: Float) {
        val key = findKeyAt(x, y)
        if (key != null) {
            pressedKeys.add(key.code)
            showPopup(key)
            view.invalidate()
        }
    }

    private fun handleTouchUp(x: Float, y: Float) {
        hidePopup()
        val key = findKeyAt(x, y)
        if (key != null) {
            pressedKeys.remove(key.code)
            keyActionListener.onKeyPress(key.code)
            view.invalidate()
        }
        pressedKeys.clear()
        view.invalidate()
    }

    private fun handleTouchMove(x: Float, y: Float) {
        hidePopup()
    }

    private fun showPopup(key: KeyInfo) {
        popupKey = key
        val rect = keyRects[key.code] ?: return
        popupRect = RectF(rect.left, rect.top - rect.height() * 1.5f, rect.right, rect.top)
        view.invalidate()
    }

    private fun hidePopup() {
        popupKey = null
        popupRect = null
        view.invalidate()
    }

    private fun findKeyAt(x: Float, y: Float): KeyInfo? {
        for ((code, rect) in keyRects) { if (rect.contains(x, y)) return keys.find { it.code == code } }
        return null
    }

    fun onStartInput(info: EditorInfo?) {
        currentKeyboardType = when { info == null -> KeyboardType.QWERTY; (info.inputType and EditorInfo.TYPE_MASK_CLASS) == EditorInfo.TYPE_CLASS_NUMBER -> KeyboardType.NUMPAD; else -> KeyboardType.QWERTY }
        buildKeys(); view.invalidate()
    }

    fun onFinishInput() { pressedKeys.clear(); view.invalidate() }
    fun setPasswordMode(isPassword: Boolean) { isPasswordMode = isPassword }
    fun switchKeyboardType(type: KeyboardType) { currentKeyboardType = type; buildKeys(); view.invalidate() }
}
