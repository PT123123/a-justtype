# Shell Keyboard - Justfile

default:
    @just --list

GRADLE := "./gradlew.bat"
APK_PATH := "app/build/outputs/apk/debug/app-debug.apk"

# Build debug APK
build:
    {{GRADLE}} assembleDebug

# Build and install
install:
    {{GRADLE}} assembleDebug
    adb install -r {{APK_PATH}}

# Run unit tests
test:
    {{GRADLE}} test

# Clean
clean:
    {{GRADLE}} clean

# Uninstall
uninstall:
    adb uninstall com.justtype.shellkeyboard

# Build, install, launch
run: install
    adb shell am start -n com.justtype.shellkeyboard/.SettingsActivity

# Logcat
logcat:
    adb logcat -s ShellKeyboard:* AndroidRuntime:* *:E

# Devices
devices:
    adb devices

# Rebuild
rebuild: clean build
