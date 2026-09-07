const fs = require('fs');
const p = 'C:/Users/ted/project/a-justtype/keyboard-ui/src/main/java/com/justtype/shellkeyboard/keyboard/ui/VimMode.kt';
let s = fs.readFileSync(p, 'utf8');

// 1. Add SystemClock import
s = s.replace('import android.view.KeyEvent\nimport com.justtype.shellkeyboard.keyboard.ui.InputConnectionBridge',
  'import android.view.KeyEvent\nimport android.os.SystemClock\nimport com.justtype.shellkeyboard.keyboard.ui.InputConnectionBridge');

// 2. Fix regex escapes: replace single backslash with double backslash in regex patterns
// The file currently has Regex(" \\s+\\S\) (illegal escape), need Regex(\\\\\s+\\\\S\)
s = s.split('Regex(\\\s+\\S\)').join('Regex(\\\\\s+\\\\S\)');
s = s.split('Regex(\\\S\\s+\)').join('Regex(\\\\\S\\\\s+\)');

// 3. Fix KeyEvent.obtain -> KeyEvent constructor with SystemClock
s = s.split('ic.sendKeyEvent(KeyEvent.obtain(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, 0, 0));')
 .join('val now = SystemClock.uptimeMillis()\n ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, 0, 0));');
s = s.split('ic.sendKeyEvent(KeyEvent.obtain(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_Z, 0, 0));')
 .join('ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_Z, 0, 0));');

fs.writeFileSync(p, s);
console.log('Fixed!');
