plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.juntardinheiro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.juntardinheiro"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(files("libs\\mysql-connector-j-8.3.0.jar"))
    implementation(libs.volley)
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation(libs.protolite.well.known.types)
    testImplementation(libs.junit);
    androidTestImplementation(libs.ext.junit);
    androidTestImplementation(libs.espresso.core);




}