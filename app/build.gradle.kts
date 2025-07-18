plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "mobprog.uts.putricellyenda"
    compileSdk = 34

    defaultConfig {
        applicationId = "mobprog.uts.putricellyenda"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    implementation("com.android.volley:volley:1.2.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.json:json:20210307")

}