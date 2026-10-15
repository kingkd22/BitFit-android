plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.bitfit"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.bitfit"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Fragment & Lifecycle & Coroutines
    implementation(libs.fragment.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.coroutines)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
