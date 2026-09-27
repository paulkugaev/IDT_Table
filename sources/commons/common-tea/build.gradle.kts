plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.badmanners.idttable.tea"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 29
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.modo.compose)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
}