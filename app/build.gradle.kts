plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.fadhil.storyapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.fadhil.storyapp"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val versions by rootProject.extra(
    mapOf(
        /** jetpack */
        "core_ktx_version" to "1.10.1",
        "appcompat_version" to "1.4.1",
        "activity_ktx_version" to "1.4.0",
        "constraintlayout_version" to "2.1.4",
        "cardview_version" to "1.0.0",
        "lifecycle_ktx_version" to "2.4.1",

        /** material design */
        "material_version" to "1.9.0",

        /** testing */
        "junit_version" to "4.13.2",
        "junit_ext_version" to "1.1.3",
        "espresso_core_version" to "3.4.0",

        /** coroutines */
        "kotlinx_coroutines_version" to "1.6.4",

        /** local storage */
        "room_version" to "2.4.0-beta01",

        /** networking */
        "gson_version" to "2.9.1",
        "retrofit_version" to "2.9.0",
        "converter_gson_version" to "2.9.0",
        "logging_interceptor_version" to "5.0.0-alpha.5",

        /** dependencies injection */
        "dagger_version" to "2.44",

        /** map struct */
        "mapstruct_kotlin_version" to  "1.4.0.0",
        "maps_struct_version" to "1.5.3.Final",

        /** supporting lib */
        "timber_version" to "5.0.1",
        "circleimageview_version" to "3.1.0",
        "glide_version" to "4.13.1",
        "compressor_version" to "3.0.1",
    )
)

dependencies {

    /** jetpack */
    implementation("androidx.core:core-ktx:${versions["core_ktx_version"]}")
    implementation("androidx.appcompat:appcompat:${versions["appcompat_version"]}")
    implementation("androidx.activity:activity-ktx:${versions["activity_ktx_version"]}")
    implementation("androidx.constraintlayout:constraintlayout:${versions["constraintlayout_version"]}")
    implementation("androidx.cardview:cardview:${versions["cardview_version"]}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${versions["lifecycle_ktx_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${versions["lifecycle_ktx_version"]}")

    /** material design */
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.android.material:material:${versions["material_version"]}")

    /** navigation */
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    /** testing */
    testImplementation("junit:junit:${versions["junit_version"]}")
    androidTestImplementation("androidx.test.ext:junit:${versions["junit_ext_version"]}")

    /** coroutines */
    androidTestImplementation("androidx.test.espresso:espresso-core:${versions["espresso_core_version"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions["kotlinx_coroutines_version"]}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions["kotlinx_coroutines_version"]}")

    /** local storage */
    implementation("androidx.room:room-runtime:${versions["room_version"]}")
    kapt("androidx.room:room-compiler:${versions["room_version"]}")
    implementation("androidx.room:room-ktx:${versions["room_version"]}")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    /** networking */
    implementation("com.google.code.gson:gson:${versions["gson_version"]}")
    implementation("com.squareup.retrofit2:retrofit:${versions["retrofit_version"]}")
    implementation("com.squareup.retrofit2:converter-gson:${versions["converter_gson_version"]}")
    implementation("com.squareup.okhttp3:logging-interceptor:${versions["logging_interceptor_version"]}")

    /** dependencies injection */
    implementation("com.google.dagger:hilt-android:${versions["dagger_version"]}")
    kapt("com.google.dagger:hilt-android-compiler:${versions["dagger_version"]}")

    /** map struct */
    api("com.github.pozo:mapstruct-kotlin:${versions["mapstruct_kotlin_version"]}")
    kapt("com.github.pozo:mapstruct-kotlin-processor:${versions["mapstruct_kotlin_version"]}")
    implementation("org.mapstruct:mapstruct:${versions["maps_struct_version"]}")
    kapt("org.mapstruct:mapstruct-processor:${versions["maps_struct_version"]}")

    /** supporting lib */
    implementation("com.jakewharton.timber:timber:${versions["timber_version"]}")
    implementation("de.hdodenhof:circleimageview:${versions["circleimageview_version"]}")
    implementation("com.github.bumptech.glide:glide:${versions["glide_version"]}")
    implementation("id.zelory:compressor:${versions["compressor_version"]}")
}
