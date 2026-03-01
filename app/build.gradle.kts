plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "com.example.androidassessment"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.androidassessment"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // ─── Flavor Dimensions ────────────────────────────────────────────────────
    flavorDimensions += "environment"

    productFlavors {
        // "dev" instead of "debug" — flavor names cannot collide with build type names
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "true")
            buildConfigField("boolean", "USE_MOCK_DATA", "true")
        }
        create("mock") {
            dimension = "environment"
            applicationIdSuffix = ".mock"
            versionNameSuffix = "-mock"
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "true")
            buildConfigField("boolean", "USE_MOCK_DATA", "true")
        }
        // "prod" instead of "release" — same reason
        create("prod") {
            dimension = "environment"
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "false")
            buildConfigField("boolean", "USE_MOCK_DATA", "false")
        }
    }

    // ─── Build Types ───────────────────────────────────────────────────────────
    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    // ─── Build Features ────────────────────────────────────────────────────────
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // ── Core AndroidX ─────────────────────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // ── Hilt ──────────────────────────────────────────────────────────────────
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // ── Retrofit & OkHttp ─────────────────────────────────────────────────────
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // ── Gson ──────────────────────────────────────────────────────────────────
    implementation(libs.gson)

    // ── Room ──────────────────────────────────────────────────────────────────
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // ── Lifecycle & ViewModel ─────────────────────────────────────────────────
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    // ── Coroutines ────────────────────────────────────────────────────────────
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // ── Timber ────────────────────────────────────────────────────────────────
    implementation(libs.timber)

    // ── SwipeRefreshLayout ────────────────────────────────────────────────────
    implementation(libs.androidx.swiperefreshlayout)

    // ── Shimmer ───────────────────────────────────────────────────────────────
    implementation(libs.facebook.shimmer)

    // ── Navigation Component ──────────────────────────────────────────────────
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // ── Testing ───────────────────────────────────────────────────────────────
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}