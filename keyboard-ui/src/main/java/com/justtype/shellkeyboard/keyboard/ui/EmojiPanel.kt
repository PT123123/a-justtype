package com.justtype.shellkeyboard.keyboard.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView

/**
 * Emoji selection panel.
 * 
 * Displays emojis in a grid for quick selection.
 * Categories: Smileys, Animals, Food, Activities, Travel, Objects, Symbols
 */
class EmojiPanel(private val context: Context) {

    val view: View
    private val grid: GridLayout

    private val categories = mapOf(
        "😀" to listOf("😀", "😃", "😄", "😁", "😆", "😅", "🤣", "😂", "🙂", "😊", "😇", "🥰", "😍", "🤩", "😘", "😗", "😚", "😙", "🥲", "😋", "😛", "😜", "🤪", "😝", "🤑", "🤗", "🤭", "🤫", "🤔", "🫡"),
        "🐶" to listOf("🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐻‍❄️", "🐨", "🐯", "🦁", "🐮", "🐷", "🐸", "🐵", "🙈", "🙉", "🙊", "🐒", "🐔", "🐧", "🐦", "🐤", "🦄", "🐝", "🦋", "🐛", "🐌", "🐞"),
        "🍎" to listOf("🍎", "🍐", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓", "🫐", "🍈", "🍒", "🍑", "🥭", "🍍", "🥥", "🥝", "🍅", "🍆", "🥑", "🥦", "🥬", "🥒", "🌶️", "🫑", "🌽", "🥕", "🫒", "🧄", "🧅", "🥔"),
        "⚽" to listOf("⚽", "🏀", "🏈", "⚾", "🥎", "🎾", "🏐", "🏉", "🥏", "🎱", "🪀", "🏓", "🏸", "🏒", "🥅", "⛳", "🪁", "🏹", "🎣", "🤿", "🥊", "🥋", "🎽", "🛹", "🛷", "⛸️", "🥌", "🎿", "⛷️", "🏂"),
        "🚗" to listOf("🚗", "🚕", "🚙", "🚌", "🚎", "🏎️", "🚓", "🚑", "🚒", "🚐", "🛻", "🚚", "🚛", "🚜", "🏍️", "🛵", "🚲", "🛴", "🛺", "🚂", "✈️", "🚀", "🛸", "🚁", "🛶", "⛵", "🚤", "🛥️", "🛳️", "🚢"),
        "💡" to listOf("💡", "🔦", "🕯️", "🪔", "🧯", "🛢️", "💸", "💵", "💴", "💶", "💷", "🪙", "💰", "💳", "💎", "⚖️", "🪜", "🧰", "🪛", "🔧", "🔨", "⚒️", "🛠️", "⛏️", "🪚", "🔩", "⚙️", "🪤", "🧲", "🔫"),
        "❤️" to listOf("❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔", "❤️‍🔥", "❤️‍🩹", "💕", "💞", "💓", "💗", "💖", "💘", "💝", "💟", "☮️", "✝️", "☪️", "🕉️", "☸️", "✡️", "🔯", "🕎", "☯️", "☦️")
    )

    private var currentCategory = "😀"

    init {
        grid = GridLayout(context).apply {
            columnCount = 6
            // alignment = Gravity.CENTER
        }
        view = grid
        showCategory(currentCategory)
    }

    /**
     * Show emojis for a category.
     */
    fun showCategory(category: String) {
        currentCategory = category
        grid.removeAllViews()
        val emojis = categories[category] ?: return
        emojis.forEach { emoji ->
            val tv = TextView(context).apply {
                text = emoji
                textSize = 24f
                gravity = Gravity.CENTER
                setPadding(16, 16, 16, 16)
                setOnClickListener {
                    onEmojiSelected(emoji)
                }
            }
            grid.addView(tv, GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            })
        }
    }

    /**
     * Get category icons.
     */
    fun getCategoryIcons(): List<String> = categories.keys.toList()

    /**
     * Called when an emoji is selected.
     */
    var onEmojiSelected: (String) -> Unit = {}
}
