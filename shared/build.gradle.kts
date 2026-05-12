plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
    id("org.jetbrains.kotlin.plugin.compose") version libs.versions.kotlin.get()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
            // Required when using NativeSQLiteDriver
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        androidMain.dependencies{
            implementation(libs.compose.runtime)
        }
        commonMain.dependencies {
            implementation(libs.coroutines.core)

            // Room KMP dependencies
            implementation(libs.room.runtime) // ✅ brings RoomDatabaseConstructor
            implementation(libs.room.common)

            implementation(libs.koin.core)
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    jvmToolchain(17)

}

android {
    namespace = "com.dbtechprojects.dailywrestlequiz"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
        val encryptionKey = project.findProperty("encryption_key")
            ?: throw GradleException("encryption_key not found in gradle.properties")
        buildConfigField("String", "ENCRYPTION_KEY", "\"${encryptionKey}\"")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        android.buildFeatures.buildConfig=true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    // ✅ Keep KSP per target — using the same Room version from TOML
    add("kspAndroid", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
}

// ✅ Exclude old IntelliJ annotations globally
configurations.all {
    exclude(group = "com.intellij", module = "annotations")
}
