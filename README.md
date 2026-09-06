# Shell Keyboard

An Android IME (Input Method Editor) based on the RIME engine.

## Features

- Full RIME engine integration via JNI
- Custom keyboard rendering
- Pinyin, Wubi, Double Pinyin input schemas
- Programmer-friendly keyboard layout
- LAN sync for user dictionary
- Privacy-first: offline by default

## Project Structure

- `app/` - APK entry point
- `ime-service/` - System IME service layer
- `core-engine/` - RIME JNI integration
- `keyboard-ui/` - Custom keyboard UI
- `candidates/` - Candidate word management
- `dict-config/` - RIME configuration and dictionaries
- `settings/` - Settings activity
- `sync/` - LAN sync server
- `data/` - Data layer (Room, DataStore)

## Building

1. Copy `local.properties.template` to `local.properties` and set SDK path
2. Place `librime.so` in `core-engine/src/main/jniLibs/arm64-v8a/` and `armeabi-v7a/`
3. Run `./gradlew assembleDebug`

## License

MIT
