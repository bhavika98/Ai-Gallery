plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.app.incroyable.ai_gallery"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.incroyable.ai_gallery"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":metadata"))
    implementation(project(":mylibrary"))
    implementation(project(":easyvideoplayer"))

    implementation(libs.material)
    implementation(libs.androidx.recyclerview)

    implementation(libs.events)
    implementation(libs.gesture.views)
    implementation(libs.android.commons)

    implementation(libs.gson)
    implementation(libs.glide)
    implementation(libs.dexter)
    implementation(libs.picker)
    implementation(libs.library)
    implementation(libs.utils.v4)
    implementation(libs.compressor)
    implementation(libs.android.image.slider)
    implementation(libs.android.image.cropper)

    implementation(libs.flickrj.android)
    implementation(libs.android.gpuimage)
}