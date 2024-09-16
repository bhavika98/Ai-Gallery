plugins {
    id("com.android.library")
}

android {
    namespace = "com.app.mylib"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.glide)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)

    implementation(libs.events)
    implementation(libs.gesture.views)
    implementation(libs.android.commons)

    implementation(libs.flickrj.android)
    implementation(libs.android.gpuimage)
}