plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
}

android {
    compileSdk = Sdk.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = Sdk.MIN_SDK_VERSION
        targetSdk = Sdk.TARGET_SDK_VERSION

        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        // Enables Jetpack Compose for this module
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = SupportLibs.AndroidX.Compose.version
    }

    lint {
        warningsAsErrors = false
        abortOnError = true
    }

    // Use this block to configure different flavors
//    flavorDimensions("version")
//    productFlavors {
//        create("full") {
//            dimension = "version"
//            applicationIdSuffix = ".full"
//        }
//        create("demo") {
//            dimension = "version"
//            applicationIdSuffix = ".demo"
//        }
//    }
}

dependencies {

    // Kotlin
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Coroutines.android)

    implementation(SupportLibs.ANDROIDX_APPCOMPAT)
    implementation(SupportLibs.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation(SupportLibs.ANDROIDX_CORE_KTX)

    testImplementation(TestingLib.JUNIT)

    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT_KTX)
    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_RULES)
    androidTestImplementation(AndroidTestingLib.ESPRESSO_CORE)

    // Dagger
    implementation(Libs.Dagger.hilt)
    kapt(Libs.Dagger.hiltCompiler)

    // Compose
    implementation(SupportLibs.AndroidX.Compose.layout)
    implementation(SupportLibs.AndroidX.Compose.material)
    implementation(SupportLibs.AndroidX.Compose.materialIconsExtended)
    implementation(SupportLibs.AndroidX.Compose.tooling)
    implementation(SupportLibs.AndroidX.Compose.uiUtil)
    implementation(SupportLibs.AndroidX.Compose.runtime)
    implementation(SupportLibs.AndroidX.Compose.runtimeLivedata)
    implementation(SupportLibs.AndroidX.Compose.viewBinding)

    // AndroidX
    implementation(SupportLibs.AndroidX.Activity.activityCompose)
    implementation(SupportLibs.AndroidX.coreKtx)
    implementation(SupportLibs.AndroidX.appcompat)
    implementation(SupportLibs.AndroidX.Lifecycle.livedata)
    implementation(SupportLibs.AndroidX.Lifecycle.viewModelCompose)
    implementation(SupportLibs.material)

    // Data Store
    implementation(Libs.DataStore.core)

    // Serialization
    implementation(Libs.KotlinxSerialization.core)

    // Timber
    implementation(Libs.timber)

    // Retrofit
    implementation(Libs.Retrofit.core)
    implementation(Libs.Retrofit.converter)
    implementation(Libs.httpLoggingInterceptor)

    // Room
    implementation(Libs.Room.core)
    kapt(Libs.Room.compiler)
    implementation(Libs.Room.room_ktx)
    implementation(Libs.Room.pagination)

    // Json serializer
    implementation(Libs.JsonSerializer.core)
}
