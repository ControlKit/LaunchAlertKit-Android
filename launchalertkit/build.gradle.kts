import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("jacoco")

}
version = "0.0.1"
android {
    namespace = "com.sepanta.controlkit.launchalertkit"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        buildConfigField("int", "LIB_VERSION_CODE", "1")
        buildConfigField("String", "LIB_VERSION_NAME", "\"${project.version}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { localProperties.load(it) }
        }

        val apiUrl = localProperties.getProperty("API_URL") ?: "https://example.com/api/launch-alert"
        buildConfigField("String", "API_URL", "\"$apiUrl\"")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
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
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(libs.androidx.material3)
    implementation(libs.errorhandler)
    implementation(libs.androidx.security.crypto)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.coil.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //REST - APIService

    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation(libs.converter.gson)
    debugImplementation(libs.ui.tooling)

    androidTestImplementation (libs.androidx.core)
    testImplementation (libs.androidx.core)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.robolectric)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.coroutines.test)
}
jacoco {
    toolVersion = "0.8.10"
}

tasks.withType<Test> {
    useJUnit()
    finalizedBy("jacocoTestReport")
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    // Add explicit dependencies to fix Gradle validation issues
    mustRunAfter("generateDebugAndroidTestResValues")
    mustRunAfter("syncDebugLibJars")
    mustRunAfter("syncReleaseLibJars")
    mustRunAfter("generateDebugAndroidTestLintModel")
    mustRunAfter("lintAnalyzeDebugAndroidTest")
    mustRunAfter("mergeReleaseResources")
    mustRunAfter("extractProguardFiles")
    mustRunAfter("mergeDebugJavaResource")
    mustRunAfter("mergeDebugJniLibFolders")
    mustRunAfter("mergeReleaseJniLibFolders")
    mustRunAfter("mergeReleaseJavaResource")
    mustRunAfter("copyDebugJniLibsProjectAndLocalJars")
    mustRunAfter("copyReleaseJniLibsProjectAndLocalJars")
    mustRunAfter("copyDebugJniLibsProjectOnly")
    mustRunAfter("copyReleaseJniLibsProjectOnly")
    mustRunAfter("generateDebugLintModel")
    mustRunAfter("generateReleaseLintModel")
    mustRunAfter("lintAnalyzeDebug")
    mustRunAfter("generateDebugLintReportModel")
    mustRunAfter("generateReleaseLintVitalModel")
    mustRunAfter("lintVitalAnalyzeRelease")
    mustRunAfter("bundleLibRuntimeToDirDebug")
    mustRunAfter("bundleLibRuntimeToDirRelease")
    mustRunAfter("generateDebugUnitTestLintModel")
    mustRunAfter("lintAnalyzeDebugUnitTest")
    mustRunAfter("verifyReleaseResources")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val mainSrc = files(
        "${project.projectDir}/src/main/java",
        "${project.projectDir}/src/main/kotlin"
    )

    val debugTree = files(
        fileTree("${buildDir}/tmp/kotlin-classes/debug") {
            exclude(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*"
            )
        },
        fileTree("${buildDir}/intermediates/javac/debug/classes") {
            exclude(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*"
            )
        }
    )

    sourceDirectories.setFrom(mainSrc)
    classDirectories.setFrom(debugTree)
    executionData.setFrom(fileTree(buildDir) {
        include("**/*.exec", "**/*.ec")
    })
}