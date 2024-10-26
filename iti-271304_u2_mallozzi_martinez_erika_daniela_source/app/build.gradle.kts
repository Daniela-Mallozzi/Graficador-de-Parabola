plugins {
    alias(libs.plugins.android.application)
    kotlin("android") // Asegúrate de incluir el plugin de Kotlin para Android
}

android {
    namespace = "com.z_iti_271304_u2_mallozzi_martinez_erika_daniela"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.z_iti_271304_u2_mallozzi_martinez_erika_daniela"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
