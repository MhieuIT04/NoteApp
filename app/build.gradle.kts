plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.noteapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.noteapp"
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
    implementation(libs.appcompat.resources)
    implementation(libs.constraintlayout)
    implementation("com.google.firebase:firebase-bom:32.1.1")
    implementation("io.grpc:grpc-okhttp:1.53.0")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("com.firebaseui:firebase-ui-firestore:8.0.1")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.core:core:1.13.1")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.functions)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}