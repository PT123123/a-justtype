# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguard_files setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep RIME native methods
-keep class com.justtype.shellkeyboard.core.Rime { *; }
-keep class com.justtype.shellkeyboard.core.RimeSession { *; }
