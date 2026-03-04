import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = libs.versions.project.namespace.get()
    compileSdk = libs.versions.project.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.project.applicationId.get()
        minSdk = libs.versions.project.minSdk.get().toInt()
        targetSdk = libs.versions.project.targetSdk.get().toInt()
        versionCode = libs.versions.project.versionCode.get().toInt()
        versionName = libs.versions.project.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    bundle {
        language { enableSplit = false }
        density { enableSplit = true }
        abi { enableSplit = true }
    }
}

base {
    val formattedDate = SimpleDateFormat("MM.dd.yyyy").format(Date())
    val versionName = libs.versions.project.versionName.get()
    archivesName.set("${rootProject.name}_v${versionName}_$formattedDate")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_18)
        // Nếu bạn muốn dùng các option khác như freeCompilerArgs:
        // freeCompilerArgs.add("-Xopt-in=kotlin.RequiresOptIn")
    }
}

configurations {
    configureEach {
        exclude(group = "com.google.android.play", module = "core")
    }
}

dependencies {
    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.datastore)
    implementation(libs.material)

    // UI Helpers
    implementation(libs.intuit.ssp)
    implementation(libs.intuit.sdp)
    implementation(libs.circle.imageview)
    implementation(libs.facebook.shimmer)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // Ads & Mediation
    implementation(libs.bundles.ads.core)
    implementation(libs.bundles.ads.mediation)
    implementation(libs.bundles.google.play)

    // Navigation & Lifecycle
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.navigation.fragment)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)

    // Hilt & Room
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Network (Retrofit + OkHttp)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.core)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    // Media (Glide, Lottie, Coil Bundle)
    implementation(libs.glide.core)
    ksp(libs.glide.compiler)
    implementation(libs.lottie.core)
    implementation(libs.lottie.compose)
    implementation(libs.bundles.coil)

    // Compose (BOM + Bundle)
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.bundles.compose)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.compose.test.manifest)

    // Others
    implementation(libs.flowext)
    implementation(libs.eventbus)
    implementation(libs.kotlinx.collections.immutable)

    // Testing
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}