plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.justtype.shellkeyboard.core"
    compileSdk = 35
    buildToolsVersion = "35.0.0"
    defaultConfig { minSdk = 28; targetSdk = 35 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { buildConfig = true }
    // TODO: Add externalNativeBuild when librime.so is available
    // TODO: Add jniLibs sourceSet when librime.so is available
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
}
