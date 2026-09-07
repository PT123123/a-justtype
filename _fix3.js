const fs = require('fs');
const p = 'C:/Users/ted/project/a-justtype/keyboard-ui/src/main/java/com/justtype/shellkeyboard/keyboard/ui/VimMode.kt';
let s = fs.readFileSync(p, 'utf8');
const BS = String.fromCharCode(92);

// 1. Add SystemClock import
s = s.replace('import android.view.KeyEvent\nimport com.justtype.shellkeyboard.keyboard.ui.InputConnectionBridge',
  'import android.view.KeyEvent\nimport android.os.SystemClock\nimport com.justtype.shellkeyboard.keyboard.ui.InputConnectionBridge');

// 2. Fix regex escapes
const old1 = 'Regex("  + BS +  s+ + BS + S\)';
const new1 = 'Regex(\ + BS + BS + s+ + BS + BS + S\)';
const old2 = 'Regex(\ + BS + S + BS + s+\)';
const new2 = 'Regex(\ + BS + BS + S + BS + BS + s+\)';
s = s.split(old1).join(new1);
s = s.split(old2).join(new2);

// 3. Fix KeyEvent.obtain
const oldDown = 'ic.sendKeyEvent(KeyEvent.obtain(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, 0, 0));';
const newDown = 'val now = SystemClock.uptimeMillis()' + '\n' + ' ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, 0, 0));';
const oldUp = 'ic.sendKeyEvent(KeyEvent.obtain(0, 0, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_Z, 0, 0));';
const newUp = 'ic.sendKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_Z, 0, 0));';
s = s.split(oldDown).join(newDown);
s = s.split(oldUp).join(newUp);

fs.writeFileSync(p, s);
console.log('Fixed!');
