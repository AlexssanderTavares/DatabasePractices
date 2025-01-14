import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("com.mysql:mysql-connector-j:9.1.0")
            implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core: 2.0.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android: 1.3.9")
            implementation("org.jetbrains.compose.ui:ui-tooling-preview: 1.4.1")
        }

        commonTest.dependencies {
            implementation("junit:junit:4.13.2")
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "org.example.cahousing.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.cahousing"
            packageVersion = "1.0.0"
        }
    }
}
