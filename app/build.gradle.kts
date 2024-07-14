plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization")
    id("de.mannodermaus.android-junit5")
}

android {
    namespace = "anridiaf.playground.simplemusicplayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "anridiaf.playground.simplemusicplayer"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    val media3Version = "1.3.1"
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-exoplayer-dash:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")

    val coilVersion = "2.6.0"
    implementation("io.coil-kt:coil:$coilVersion")

    val lottieVersion = "6.4.1"
    implementation("com.airbnb.android:lottie:$lottieVersion")

    val ktorVersion = "2.3.2"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    val koinVersion = "3.5.6"
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-android:$koinVersion")

    /**
     *  =======================================================================
     *                         UNIT TEST DEPENDENCIES
     *  =======================================================================
     */
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.insert-koin:koin-test-junit5:$koinVersion")

    val jUniVersion = "5.10.0"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUniVersion")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:$jUniVersion")

    val mockkVersion = "1.12.4"
    testImplementation("io.mockk:mockk:$mockkVersion")

    val coroutinesTestVersion = "1.8.1"
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTestVersion")

    val turbineVersion = "1.1.0"
    testImplementation("app.cash.turbine:turbine:$turbineVersion")

    /**
     *  =======================================================================
     *                         INTEGRATION TEST DEPENDENCIES
     *  =======================================================================
     */

    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}