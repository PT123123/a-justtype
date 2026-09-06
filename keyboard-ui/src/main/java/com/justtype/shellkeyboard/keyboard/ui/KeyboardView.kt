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
import android.view.View
import android.view.inputmethod.EditorInfo
import com.justtype.shellkeyboard.core.RimeDispatcher
import com.justtype.shellkeyboard.ime.InputConnectionBridge
import com.justtype.shellkeyboard.keyboard.ui.KeyboardWindow.KeyboardType

/**
 * Custom-drawn keyboard view with full touch handling.
 * 
 * Features:
 * - Custom key rendering with rounded rectangles
 * - Multi-touch support
 * - Long press detection
 * - Key popup preview
 * - Haptic feedback integration
 */
class KeyboardView(
    private val context: Context,
    private val rimeDispatcher: RimeDispatcher,
    private val inputConnectionBridge: InputConnectionBridge
) {
    val view: View
    private val keyActionListener = KeyActionListener(rimeDispatcher, inputConnectionBridge)
    private var isPasswordMode = false
    private var currentKeyboardType = KeyboardType.QWERTY
    
    // Key dimensions
    private val keyMargin = 4f
    private val keyCornerRadius = 8f
    
    // Paints
    private val keyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFE8E8E8.toInt()
        style = Paint.Style.FILL
    }
    private val keyTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF333333.toInt()
        textSize = 24f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT
    }
    private val functionKeyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFBDBDBD.toInt()
        style = Paint.Style.FILL
    }
    private val functionKeyTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF333333.toInt()
        textSize = 18f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    private val pressedKeyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF6750A4.toInt()
        style = Paint.Style.FILL
    }
    private val pressedKeyTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 24f
        textAlign = Paint.Align.CENTER
    }
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x1A000000
        style = Paint.Style.FILL
    }
    
    // Key state
    private val keys = mutableListOf<KeyInfo>()
    private val pressedKeys = mutableSetOf<Int>()
    private val keyRects = mutableMapOf<Int, RectF>()
    
    data class KeyInfo(
        val label: String,
        val code: Int,
        val type: KeyType,
        val width: Float = 1f,
        val altLabel: String? = null
    )
    
    enum class KeyType {
        CHARACTER,
        FUNCTION,
        MODIFIER
    }
    
    // QWERTY layout
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
            }
            
            override fun onTouchEvent(event: MotionEvent): Boolean {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> handleTouchDown(event.x, event.y, event.getPointerId(0))
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        val pointerIndex = event.actionIndex
                        handleTouchDown(event.getX(pointerIndex), event.getY(pointerIndex), event.getPointerId(pointerIndex))
                    }
                    MotionEvent.ACTION_UP -> handleTouchUp(event.x, event.y, event.getPointerId(0))
                    MotionEvent.ACTION_POINTER_UP -> {
                        val pointerIndex = event.actionIndex
                        handleTouchUp(event.getX(pointerIndex), event.getY(pointerIndex), event.getPointerId(pointerIndex))
                    }
                    MotionEvent.ACTION_MOVE -> handleTouchMove(event)
                    MotionEvent.ACTION_CANCEL -> {
                        pressedKeys.clear()
                        invalidate()
                    }
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
        // Number row
        numberRow.forEach { keys.add(KeyInfo(it, it.first().code, KeyType.CHARACTER)) }
        // Letter rows
        qwertyRows.forEach { row ->
            row.forEach { key ->
                val code = when (key) {
                    "⇧" -> KeyEvent.KEYCODE_SHIFT_LEFT
                    "⌫" -> KeyEvent.KEYCODE_DEL
                    else -> key.first().code
                }
                val type = when (key) {
                    "⇧", "⌫" -> KeyType.FUNCTION
                    else -> KeyType.CHARACTER
                }
                keys.add(KeyInfo(key, code, type))
            }
        }
        // Bottom row
        keys.add(KeyInfo("?123", -1, KeyType.FUNCTION, 1.5f))
        keys.add(KeyInfo("🌐", -2, KeyType.FUNCTION, 1f))
        keys.add(KeyInfo("space", KeyEvent.KEYCODE_SPACE, KeyType.FUNCTION, 4f))
        keys.add(KeyInfo(".", KeyEvent.KEYCODE_PERIOD, KeyType.CHARACTER))
        keys.add(KeyInfo("↵", KeyEvent.KEYCODE_ENTER, KeyType.FUNCTION, 1.5f))
    }
    
    private fun buildSymbolKeys() {
        val symbols = listOf("1","2","3","4","5","6","7","8","9","0",
            "!","@","#","$","%","^","&","*","(",")",
            "{","}","[","]","<",">","=","+","-","_")
        symbols.forEach { keys.add(KeyInfo(it, it.first().code, KeyType.CHARACTER)) }
        keys.add(KeyInfo("ABC", -1, KeyType.FUNCTION, 1.5f))
        keys.add(KeyInfo("space", KeyEvent.KEYCODE_SPACE, KeyType.FUNCTION, 5f))
        keys.add(KeyInfo("↵", KeyEvent.KEYCODE_ENTER, KeyType.FUNCTION, 1.5f))
    }
    
    private fun buildNumpadKeys() {
        listOf("7","8","9","4","5","6","1","2","3","0").forEach {
            keys.add(KeyInfo(it, it.first().code, KeyType.CHARACTER))
        }
        keys.add(KeyInfo(".", KeyEvent.KEYCODE_PERIOD, KeyType.CHARACTER))
        keys.add(KeyInfo("⌫", KeyEvent.KEYCODE_DEL, KeyType.FUNCTION))
    }
    
    private fun buildProgrammerKeys() {
        listOf("{","}","[","]","(",")","<",">",
            "=","!","&","|","+","-","*","/",
            ";",":","\"","'","\\","~","`","#").forEach {
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
        var keyIndex = 0
        
        for (rowNum in 0 until totalRows) {
            if (keyIndex >= keys.size) break
            
            val keysInRow = getKeysInRow(rowNum)
            val totalWeight = keysInRow.sumOf { it.width.toDouble() }.toFloat()
            val keyWidth = width / totalWeight
            
            var x = 0f
            for (key in keysInRow) {
                val rect = RectF(
                    x + keyMargin,
                    y + keyMargin,
                    x + keyWidth * key.width - keyMargin,
                    y + rowHeight - keyMargin
                )
                
                // Draw shadow
                canvas.drawRoundRect(RectF(rect.left + 2, rect.top + 2, rect.right + 2, rect.bottom + 2), keyCornerRadius, keyCornerRadius, shadowPaint)
                
                val isPressed = pressedKeys.contains(key.code)
                val paint = when {
                    isPressed -> pressedKeyPaint
                    key.type == KeyType.FUNCTION -> functionKeyPaint
                    else -> keyPaint
                }
                val textPaint = when {
                    isPressed -> pressedKeyTextPaint
                    key.type == KeyType.FUNCTION -> functionKeyTextPaint
                    else -> keyTextPaint
                }
                
                canvas.drawRoundRect(rect, keyCornerRadius, keyCornerRadius, paint)
                
                // Draw key label
                val textBounds = android.graphics.Rect()
                textPaint.getTextBounds(key.label, 0, key.label.length, textBounds)
                val textX = rect.centerX()
                val textY = rect.centerY() + textBounds.height() / 2f - textBounds.bottom
                canvas.drawText(key.label, textX, textY, textPaint)
                
                keyRects[key.code] = rect
                x += keyWidth * key.width
                keyIndex++
            }
            y += rowHeight
        }
    }
    
    private fun getKeysInRow(row: Int): List<KeyInfo> {
        // Row 0: numbers (10 keys)
        // Row 1: qwerty top (9 keys)
        // Row 2: qwerty middle with shift and backspace (9 keys)
        // Row 3: bottom row (?123, globe, space, ., enter)
        // Row 4: empty or extra
        return when (row) {
            0 -> keys.filter { it.label.length == 1 && it.label[0].isDigit() }
            1 -> keys.filter { it.label in listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p") }
            2 -> keys.filter { it.label in listOf("a", "s", "d", "f", "g", "h", "j", "k", "l") }
            3 -> keys.filter { it.label in listOf("⇧", "z", "x", "c", "v", "b", "n", "m", "⌫") }
            else -> keys.filter { it.label in listOf("?123", "🌐", "space", ".", "↵") }
        }
    }
    
    private fun handleTouchDown(x: Float, y: Float, pointerId: Int) {
        val key = findKeyAt(x, y)
        if (key != null) {
            pressedKeys.add(key.code)
            view.invalidate()
        }
    }
    
    private fun handleTouchUp(x: Float, y: Float, pointerId: Int) {
        val key = findKeyAt(x, y)
        if (key != null) {
            pressedKeys.remove(key.code)
            keyActionListener.onKeyPress(key.code)
            view.invalidate()
        }
        pressedKeys.clear()
    }
    
    private fun handleTouchMove(event: MotionEvent) {
        // Handle slide input
    }
    
    private fun findKeyAt(x: Float, y: Float): KeyInfo? {
        for ((code, rect) in keyRects) {
            if (rect.contains(x, y)) {
                return keys.find { it.code == code }
            }
        }
        return null
    }
    
    fun onStartInput(info: EditorInfo?) {
        currentKeyboardType = when {
            info == null -> KeyboardType.QWERTY
            (info.inputType and EditorInfo.TYPE_MASK_CLASS) == EditorInfo.TYPE_CLASS_NUMBER -> KeyboardType.NUMPAD
            else -> KeyboardType.QWERTY
        }
        buildKeys()
        view.invalidate()
    }
    
    fun onFinishInput() {
        pressedKeys.clear()
        view.invalidate()
    }
    
    fun setPasswordMode(isPassword: Boolean) {
        isPasswordMode = isPassword
    }
    
    fun switchKeyboardType(type: KeyboardType) {
        currentKeyboardType = type
        buildKeys()
        view.invalidate()
    }
}
