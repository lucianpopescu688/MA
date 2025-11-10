plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Adaugă această linie:
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.servicebuddy"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.servicebuddy"
        minSdk = 26 // Rulează pe Android 8.0 și mai nou
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Am scos 'buildFeatures { compose = true }'
    // E important să NU ai 'compose = true' aici
    buildFeatures {
        viewBinding = false // Sau șterge complet secțiunea buildFeatures
    }
}

dependencies {
    // Dependințe de bază
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.appcompat:appcompat:1.6.1") // << LINIA CORECTATĂ
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Material Design
    implementation("com.google.android.material:material:1.12.0")

    // ViewModel și LiveData (pentru MVVM)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.1")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.8.0")

    // Navigation Component (pentru fragmente separate)
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // RecyclerView (pentru listă)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}