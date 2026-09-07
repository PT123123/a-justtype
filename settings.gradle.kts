pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "shell-keyboard"

include(":app")
include(":core-engine")
include(":ime-service")
include(":keyboard-ui")
include(":candidates")
include(":dict-config")
include(":settings")
include(":sync")
include(":data")