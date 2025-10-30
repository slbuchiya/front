plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.smartfit2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.smartfit2"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material) // આ જ લાઇબ્રેરી જે એરર આપતી હતી
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // ચાર્ટ માટે (તમારા પ્રોજેક્ટમાં હતી, તેથી રાખી છે)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // --- Retrofit (ફક્ત આ જ API લાઇબ્રેરી રાખવાની છે) ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    // ------------------------------------

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

