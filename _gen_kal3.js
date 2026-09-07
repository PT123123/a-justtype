
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

// KeyActionListener.kt
wf(UI + 'KeyActionListener.kt', [
'package com.justtype.shellkeyboard.keyboard.ui',
'',
'import android.view.KeyEvent',
'import com.justtype.shellkeyboard.core.RimeDispatcher',
'',
'class KeyActionListener(',
'    private val rimeDispatcher: RimeDispatcher,',
'    private val inputConnectionBridge: InputConnectionBridge,',
'    private val vimMode: VimMode? = null,',
'    private val bracketPair: BracketPair? = null,',
'    private val markdownShortcuts: MarkdownShortcuts? = null,',
'    private val onRimeKeyProcessed: ((Boolean) -> Unit)? = null',
') {',
'    fun onKeyPress(keyCode: Int, modifiers: Int = 0): Boolean {',
'        if (vimMode?.isEnabled == true) {',
'            if (vimMode.onKeyPress(keyCode)) return true',
'        }',
'        when (keyCode) {',
'            KeyEvent.KEYCODE_DEL -> { inputConnectionBridge.deleteSurroundingText(1, 0); return true }',
'            KeyEvent.KEYCODE_ENTER -> { inputConnectionBridge.sendEnter(); return true }',
'            KeyEvent.KEYCODE_SPACE -> { onRimeKeyProcessed?.invoke(false); inputConnectionBridge.commitText(" "); return true }',
'        }',
'        markdownShortcuts?.let { md ->',
'            val char = getCharForKeyCode(keyCode, modifiers)',
'            if (char != null && md.process(char)) return true',
'        }',
'        bracketPair?.let { bp ->',
'            val char = getCharForKeyCode(keyCode, modifiers)',
'            if (char != null && bp.process(char)) return true',
'        }',
'        val char = getCharForKeyCode(keyCode, modifiers)',
'        if (char != null) {',
'            rimeDispatcher.execute { }',
'            inputConnectionBridge.commitText(char)',
'            onRimeKeyProcessed?.invoke(true)',
'            return true',
'        }',
'        return false',
'    }',
'',
'    private fun getCharForKeyCode(keyCode: Int, modifiers: Int): String? {',
'        val isShift = modifiers and KeyEvent.META_SHIFT_ON != 0',
'        return when (keyCode) {',
'            in KeyEvent.KEYCODE_A..KeyEvent.KEYCODE_Z -> { val base = if (isShift) ' + SQ + 'A' + SQ + ' else ' + SQ + 'a' + SQ + '; (base + (keyCode - KeyEvent.KEYCODE_A)).toString() }',
'            in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> { val chars = if (isShift) ' + DQ + ')!@#$%^&*(' + DQ + ' else ' + DQ + '0123456789' + DQ + '; chars[keyCode - KeyEvent.KEYCODE_0].toString() }',
'            KeyEvent.KEYCODE_PERIOD -> ' + DQ + '.' + DQ,
'            KeyEvent.KEYCODE_COMMA -> ' + DQ + ',' + DQ,
'            KeyEvent.KEYCODE_MINUS -> if (isShift) ' + DQ + '_' + DQ + ' else ' + DQ + '-' + DQ,
'            KeyEvent.KEYCODE_SLASH -> if (isShift) ' + DQ + '?' + DQ + ' else ' + DQ + '/' + DQ,
'            KeyEvent.KEYCODE_SEMICOLON -> if (isShift) ' + DQ + ':' + DQ + ' else ' + DQ + ';' + DQ,
'            KeyEvent.KEYCODE_APOSTROPHE -> if (isShift) ' + DQ + BS + BS + BS + DQ + DQ + ' else ' + DQ + SQ + DQ,
'            KeyEvent.KEYCODE_GRAVE -> if (isShift) ' + DQ + '~' + DQ + ' else ' + DQ + BT + DQ,
'            KeyEvent.KEYCODE_LEFT_BRACKET -> if (isShift) ' + DQ + LB + DQ + ' else ' + DQ + '[' + DQ,
'            KeyEvent.KEYCODE_RIGHT_BRACKET -> if (isShift) ' + DQ + RB + DQ + ' else ' + DQ + ']' + DQ,
'            KeyEvent.KEYCODE_BACKSLASH -> if (isShift) ' + DQ + '|' + DQ + ' else ' + DQ + BS + BS + DQ,
'            KeyEvent.KEYCODE_EQUALS -> if (isShift) ' + DQ + '+' + DQ + ' else ' + DQ + '=' + DQ,
'            else -> null',
'        }',
'    }',
'}',
''
].join('\n'));

console.log('KeyActionListener done');
