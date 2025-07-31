buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
