plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    id("org.jetbrains.kotlin.plugin.compose") version libs.versions.kotlin.get() // ✅ Add this
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

android {
    signingConfigs {
        create("release") {
            // Read signing properties from the Gradle properties (gradle.properties) instead of hardcoding.
            // Use nullable lookups so the build doesn't fail if properties are missing during local dev.
            val storeFileProp: String? = project.findProperty("SIGNING_STORE_FILE") as? String
            val keyAliasProp: String? = project.findProperty("SIGNING_KEY_ALIAS") as? String
            val storePasswordProp: String? = project.findProperty("SIGNING_STORE_PASSWORD") as? String
            val keyPasswordProp: String? = project.findProperty("SIGNING_KEY_PASSWORD") as? String

            storeFile = storeFileProp?.let { file(it) }
            keyAlias = keyAliasProp ?: "key0"
            storePassword = storePasswordProp ?: ""
            keyPassword = keyPasswordProp ?: ""
        }
    }
    namespace = "com.dbtechprojects.dailywrestlequiz.android"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.dbtechprojects.dailywrestlequiz.android"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.assertions)


    // If using the ViewModel add-on library
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    // Spin Wheel
    implementation("com.github.commandiron:SpinWheelCompose:1.1.1")
}