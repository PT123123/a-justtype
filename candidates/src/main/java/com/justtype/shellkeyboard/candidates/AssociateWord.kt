package com.justtype.shellkeyboard.candidates

import com.justtype.shellkeyboard.core.RimeSession

class AssociateWord {

    private var isAssociating: Boolean = false
    private var associatedWords: List<String> = emptyList()
    private var session: RimeSession? = null

    fun setSession(session: RimeSession?) {
        this.session = session
    }

    fun startAssociation(word: String) {
        isAssociating = true
        associatedWords = queryAssociativeWords(word)
    }

    fun getAssociatedWords(): List<String> {
        return if (isAssociating) associatedWords else emptyList()
    }

    fun isAssociating(): Boolean = isAssociating

    fun stopAssociation() {
        isAssociating = false
        associatedWords = emptyList()
    }

    private fun queryAssociativeWords(word: String): List<String> {
        if (word.isEmpty()) return emptyList()
        val context = session?.getContext()
        if (context != null && context.menu.candidates.isNotEmpty()) {
            return context.menu.candidates.map { it.text }.filter { it != word }
        }
        val commonAssociations = mapOf(
            "我" to listOf("们", "的", "是", "不", "在", "有", "和", "这", "中", "大"),
            "你" to listOf("们", "的", "是", "好", "在", "有", "和", "这", "不", "会"),
            "他" to listOf("们", "的", "是", "不", "在", "有", "和", "这", "也", "会"),
            "的" to listOf("时", "候", "间", "人", "事", "物", "地", "方", "法", "子"),
            "是" to listOf("一", "不", "在", "有", "和", "人", "这", "中", "大", "为"),
            "不" to listOf("能", "会", "可", "要", "能", "过", "好", "多", "到", "在"),
            "在" to listOf("这", "那", "一", "个", "人", "心", "中", "间", "时", "地"),
            "有" to listOf("一", "个", "人", "时", "地", "方", "法", "理", "心", "力"),
            "和" to listOf("平", "气", "谐", "解", "好", "美", "善", "爱", "友", "亲"),
            "了" to listOf("解", "决", "定", "义", "意", "思", "想", "法", "人", "事")
        )
        return commonAssociations[word] ?: emptyList()
    }
}
