$inFile = "C:\Users\ted\project\a-justtype\keyboard-ui\src\main\java\com\justtype\shellkeyboard\keyboard\ui\VimMode.kt"
$outFile = $inFile
$lines = Get-Content $inFile
$newLines = @()
$addedSystemClock = $false
foreach ($line in $lines) {
    if (-not $addedSystemClock -and $line -match "import InputConnectionBridge") {
        $newLines += $line
        $newLines += "import android.os.SystemClock"
        $addedSystemClock = $true
        continue
    }
    if ($line -match "Regex\(""\\s\+\\S""\)") {
        $line = $line -replace 'Regex\(""\\s\+\\S""\)','Regex(""\\\\s+\\\\S"")"
    }
    if ($line -match "Regex\(""\\S\\s\+""\)") {
        $line = $line -replace 'Regex\(""\\S\\s\+""\)','Regex(""\\\\S\\\\s+"")"
    }
    if ($line -match "KeyEvent\.obtain") {
        $i = $newLines.Count - 1
        while ($i -ge 0 -and $newLines[$i].Trim() -eq "") { $i-- }
        $newLines += "        val now = SystemClock.uptimeMillis()"
        $line = $line -replace "KeyEvent\.obtain\(0, 0, KeyEvent\.ACTION_DOWN, KeyEvent\.KEYCODE_Z, 0, 0\)","KeyEvent(now, now, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Z, 0, 0)"
        $line = $line -replace "KeyEvent\.obtain\(0, 0, KeyEvent\.ACTION_UP, KeyEvent\.KEYCODE_Z, 0, 0\)","KeyEvent(now, now, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_Z, 0, 0)"
    }
    $newLines += $line
}
Set-Content $outFile -Value ($newLines -join "`n") -NoNewline
Write-Host "Done"
