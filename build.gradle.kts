buildscript {

    dependencies {
        classpath("com.android.tools.build:gradle:8.5.0")
        classpath("com.google.gms:google-services:4.3.15") // Add this line
    }
}
plugins {
    id("com.android.application") version "8.5.0" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false // Update version here
}

