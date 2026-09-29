plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.badmanners.idttable.feature.input.api"
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
}