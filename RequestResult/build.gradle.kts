plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
    id ("maven-publish")
}

android {
    namespace = "com.rootachieve.requestresult"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release"
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.rootachieve"
                artifactId = "RequestResult"
                version = "0.1.0"
            }
        }
    }
}