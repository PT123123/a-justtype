const fs = require('fs');
const p = 'C:/Users/ted/project/a-justtype/keyboard-ui/src/main/java/com/justtype/shellkeyboard/keyboard/ui/VimMode.kt';
const lines = fs.readFileSync(p, 'utf8').split('\n');
const BS = String.fromCharCode(92);
let newLines = [];
let addedSystemClock = false;
for (const line of lines) {
  if (!addedSystemClock && line.includes('import android.view.KeyEvent')) {
    newLines.push(line);
    newLines.push('import android.os.SystemClock');
    addedSystemClock = true;
    continue;
  }
  let l = line;
  const oldReg1 = 'Regex(' + BS + BS + BS + 's+' + BS + BS + BS + 'S' + BS + BS + ')';
  const newReg1 = 'Regex(' + BS + BS + BS + BS + BS + BS + 's+' + BS + BS + BS + BS + BS + BS + 'S' + BS + BS + ')';
  l = l.split(oldReg1).join(newReg1);
  if (l.includes('KeyEvent.obtain')) {
    if (l.includes('ACTION_DOWN')) {
      l = '        val now = SystemClock.uptimeMillis()';
    } else {
      l = '';
    }
  }
  newLines.push(l);
}
fs.writeFileSync(p, newLines.join('\n'));
console.log('Fixed!');
