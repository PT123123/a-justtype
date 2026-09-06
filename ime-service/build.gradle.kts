plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.justtype.shellkeyboard.ime"
    compileSdk = 35
    buildToolsVersion = "35.0.0"
    defaultConfig { minSdk = 28; targetSdk = 35 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { buildConfig = true }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(project(":core-engine"))
    implementation(project(":keyboard-ui"))
    implementation(project(":candidates"))
    implementation(project(":dict-config"))
    implementation(project(":data"))
    implementation(project(":settings"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.service)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
}
