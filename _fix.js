const fs = require('fs');  
const p = 'C:/Users/ted/project/a-justtype/keyboard-ui/src/main/java/com/justtype/shellkeyboard/keyboard/ui/VimMode.kt';  
let lines = fs.readFileSync(p, 'utf8').split('\n');  
let newLines = [];  
let addedSystemClock = false;  
for (const line of lines) {  
