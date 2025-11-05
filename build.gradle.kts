plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android)      apply false
    alias(libs.plugins.kotlin.compose)      apply false
    alias(libs.plugins.ksp)                 apply false

    // Hilt (không đưa vào catalog cũng được)
    id("com.google.dagger.hilt.android") version "2.57.2" apply false
}
