package com.justtype.shellkeyboard.keyboard.ui

import com.justtype.shellkeyboard.keyboard.ui.InputConnectionBridge

/**
 * Markdown shortcut expansion.
 * 
 * Shortcuts:
 * - **text** → bold
 * - *text* → italic
 * - ~~text~~ → strikethrough
 * - `code` → inline code
 * - [text](url) → link
 * - ![alt](url) → image
 * - # → heading
 * - - → list item
 * - > → blockquote
 * - --- → horizontal rule
 * - ``` → code block
 */
class MarkdownShortcuts(private val bridge: InputConnectionBridge) {

    private val shortcuts = mapOf(
        "**" to Pair("**", "**"),
        "*" to Pair("*", "*"),
        "~~" to Pair("~~", "~~"),
        "`" to Pair("`", "`"),
        "##" to Pair("# ", ""),
        "###" to Pair("## ", ""),
        "####" to Pair("### ", ""),
        "- " to Pair("- ", ""),
        "> " to Pair("> ", ""),
        "---" to Pair("---\n", ""),
        "```" to Pair("```\n", "\n```"),
        "[]" to Pair("[", "](url)"),
        "![]" to Pair("![", "](url)"),
        "[]" to Pair("[", "](url)")
    )

    /**
     * Process markdown shortcut.
     * Returns true if shortcut was expanded.
     */
    fun process(input: String): Boolean {
        val textBefore = bridge.getTextBeforeCursor(50) ?: return false
        val before = textBefore.toString()

        for ((trigger, replacement) in shortcuts) {
            if (before.endsWith(trigger)) {
                // Delete the trigger
                bridge.deleteSurroundingText(trigger.length, 0)
                // Insert with cursor in middle
                val middle = replacement.first
                val end = replacement.second
                bridge.commitText(middle + end)
                // Move cursor to middle
                bridge.moveCursor(-end.length)
                return true
            }
        }
        return false
    }

    /**
     * Insert markdown template.
     */
    fun insertTemplate(template: String) {
        bridge.commitText(template)
    }

    /**
     * Get all available templates.
     */
    fun getTemplates(): List<Pair<String, String>> = listOf(
        "Bold" to "**bold**",
        "Italic" to "*italic*",
        "Strikethrough" to "~~strike~~",
        "Code" to "`code`",
        "Link" to "[text](url)",
        "Image" to "![alt](url)",
        "H1" to "# ",
        "H2" to "## ",
        "H3" to "### ",
        "List" to "- ",
        "Quote" to "> ",
        "Divider" to "---",
        "Code Block" to "```\n\n```"
    )
}
