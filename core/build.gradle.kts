plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "ru.blackmirrror.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "BACKEND_BASE_URL", "\"${project.property("BACKEND_BASE_URL")}\"")
        buildConfigField("String", "BACKEND_HOST", "\"${project.property("BACKEND_HOST")}\"")
        buildConfigField("String", "SUPABASE_BASE_URL", "\"${project.property("SUPABASE_BASE_URL")}\"")
        buildConfigField("String", "SUPABASE_API_KEY", "\"${project.property("SUPABASE_API_KEY")}\"")
        buildConfigField("String", "SUPABASE_BUCKET_NAME", "\"${project.property("SUPABASE_BUCKET_NAME")}\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.json)
    implementation(libs.retrofit.adapters.result)
}