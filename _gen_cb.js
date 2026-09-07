
const fs = require('fs');
const path = require('path');

const BT = String.fromCharCode(96);  // backtick
const BS = String.fromCharCode(92);  // backslash  
const DQ = String.fromCharCode(34);  // double quote
const SQ = String.fromCharCode(39);  // single quote
const LB = String.fromCharCode(123); // {
const RB = String.fromCharCode(125); // }

function wf(filePath, content) {
    fs.mkdirSync(path.dirname(filePath), { recursive: true });
    fs.writeFileSync(filePath, content, 'utf-8');
    console.log('OK ' + filePath);
}

const UI = '/mnt/c/Users/ted/project/a-justtype/keyboard-ui/src/main/java/com/justtype/shellkeyboard/keyboard/ui/';

// CandidatesBar.kt
wf(UI + 'CandidatesBar.kt', [
'package com.justtype.shellkeyboard.keyboard.ui',
'',
'import android.content.Context',
'import android.view.Gravity',
'import android.view.View',
'import android.widget.HorizontalScrollView',
'import android.widget.LinearLayout',
'import android.widget.TextView',
'import android.view.inputmethod.EditorInfo',
'',
'class CandidatesBar(private val context: Context) {',
'    val view: View',
'    private val scrollView: HorizontalScrollView',
'    private val container: LinearLayout',
'    private var isPasswordMode = false',
'    private var candidates = listOf<String>()',
'    private var highlightedIndex = 0',
'    ',
'    var onCandidateSelected: ((Int) -> Unit)? = null',
'    var onNextPage: (() -> Unit)? = null',
'    var onPrevPage: (() -> Unit)? = null',
'',
'    init {',
'        scrollView = HorizontalScrollView(context).apply {',
'            isHorizontalScrollBarEnabled = false',
'        }',
'        container = LinearLayout(context).apply {',
'            orientation = LinearLayout.HORIZONTAL',
'            gravity = Gravity.CENTER_VERTICAL',
'        }',
'        scrollView.addView(container)',
'        view = scrollView',
'    }',
'',
'    fun updateCandidates(candidates: List<String>, highlightedIndex: Int = 0, pageInfo: String = "") {',
'        if (isPasswordMode) {',
'            view.visibility = View.GONE',
'            return',
'        }',
'        view.visibility = View.VISIBLE',
'        this.candidates = candidates',
'        this.highlightedIndex = highlightedIndex',
'        container.removeAllViews()',
'        ',
'        if (pageInfo.isNotEmpty()) {',
'            val prevBtn = TextView(context).apply {',
'                text = "<"',
'                textSize = 16f',
'                setPadding(24, 16, 24, 16)',
'                setOnClickListener { onPrevPage?.invoke() }',
'            }',
'            container.addView(prevBtn)',
'        }',
'        ',
'        candidates.forEachIndexed { index, candidate ->',
'            val tv = TextView(context).apply {',
'                text = candidate',
'                textSize = 18f',
'                setPadding(32, 16, 32, 16)',
'                if (index == highlightedIndex) {',
'                    setBackgroundColor(0xFF6750A4.toInt())',
'                    setTextColor(0xFFFFFFFF.toInt())',
'                }',
'                setOnClickListener { onCandidateSelected?.invoke(index) }',
'            }',
'            container.addView(tv)',
'        }',
'        ',
'        if (pageInfo.isNotEmpty()) {',
'            val nextBtn = TextView(context).apply {',
'                text = ">"',
'                textSize = 16f',
'                setPadding(24, 16, 24, 16)',
'                setOnClickListener { onNextPage?.invoke() }',
'            }',
'            container.addView(nextBtn)',
'        }',
'    }',
'',
'    fun onStartInput(info: EditorInfo?) {',
'        container.removeAllViews()',
'    }',
'',
'    fun onFinishInput() {',
'        container.removeAllViews()',
'    }',
'',
'    fun setPasswordMode(isPassword: Boolean) {',
'        isPasswordMode = isPassword',
'        view.visibility = if (isPassword) View.GONE else View.VISIBLE',
'    }',
'    ',
'    fun getCurrentCandidates(): List<String> = candidates',
'    fun getHighlightedIndex(): Int = highlightedIndex',
'}',
''
].join('\n'));

console.log('CandidatesBar done');
