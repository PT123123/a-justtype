const fs = require('fs');
const p = '<USER_HOME>/project/a-justtype/keyboard-ui/src/main/java/com/justtype/shellkeyboard/keyboard/ui/VimMode.kt';
let s = fs.readFileSync(p, 'utf8');
s = s.replace('import android.view.KeyEvent\n