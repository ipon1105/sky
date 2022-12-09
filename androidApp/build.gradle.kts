plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.sky.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.example.sky.android"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.firebase:firebase-firestore-ktx:24.4.1")

    val composeVersion = "1.2.1"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.activity:activity-compose:1.6.1")

    //Зависимость для навигацииы
    implementation("androidx.navigation:navigation-compose:2.5.3")

    //Зависимость для карты Google
    implementation ("com.google.maps.android:maps-compose:2.7.3")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    //implementation ("androidx.compose.foundation:foundation:2.7.2")
    implementation ("com.google.maps.android:maps-compose-widgets:2.7.3")

    //Firebase
    implementation ("com.google.firebase:firebase-bom:31.1.1")
    implementation ("com.google.gms:google-services:4.3.14")
    implementation ("com.google.firebase:firebase-auth-ktx:21.1.0")
    //implementation ("com.google.firebase:firebase-analytics-ktx")

    //Зависимость для ImageSlider
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")
}