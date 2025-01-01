plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

}

android {
    namespace = "uk.ac.tees.mad.c2249753"
    compileSdk = 34
    defaultConfig {
        applicationId = "uk.ac.tees.mad.c2249753"
        multiDexEnabled = true
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core libraries
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.7.6")

    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Kotlin BOM (Ensures Kotlin dependencies are aligned)
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))

    // Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Compose Navigation
    val navVersion = "2.6.0"
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // Dagger Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-compiler:2.45")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Retrofit for networking (corrected group name)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Datastore for Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Compose Foundation library
    implementation("androidx.compose.foundation:foundation:1.7.4")

    // Accompanist library for navigation
    implementation("com.google.accompanist:accompanist-navigation-material:0.31.4-beta")

    // Paging 3 (no platform usage)
    val pagingVersion = "3.1.1"
    implementation("androidx.paging:paging-runtime:$pagingVersion")
    implementation("androidx.paging:paging-compose:3.2.0-rc01")

    // Room for database handling
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:2.5.2")


    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-storage-ktx:21.0.1")
    implementation ("androidx.compose.material:material-icons-extended:1.7.5")
    implementation("com.google.firebase:firebase-auth-ktx:23.1.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:24.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7") // Ensure you have ViewModel support for Jetpack Compose

    //push notification
    implementation ("com.google.firebase:firebase-messaging:24.1.0")


    //Api calls
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("com.google.firebase:firebase-crashlytics:19.2.1")
    implementation ("com.google.firebase:firebase-analytics:22.1.2")


    implementation ("androidx.multidex:multidex:2.0.1")





}
