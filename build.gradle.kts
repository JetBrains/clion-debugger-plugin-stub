import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.intellij") version "1.17.2"
}

group "com.your.company.unique.plugin.id"
version "1.0"

repositories {
    mavenCentral()
}

dependencies {
//    implementation(libs.annotations)
}

intellij {
    plugins.set(listOf("com.intellij.clion", "com.intellij.cidr.lang","com.intellij.cidr.base", "com.intellij.nativeDebug"))
    version.set("2023.3")
    type.set("CL")
}

sourceSets {
    main {
        kotlin.srcDirs("src")
        java.srcDirs("src", "gen")
        resources.srcDirs("resources")
    }

    test {
        kotlin.srcDirs("test")
        resources.srcDirs("testData")
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.apiVersion = "1.9"
    }

    patchPluginXml {
        sinceBuild.set("233")
        untilBuild.set("233.*")
    }
}