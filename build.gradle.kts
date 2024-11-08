plugins {
    id("java")
    kotlin("jvm") version "2.0.21"
    id("org.jetbrains.intellij.platform") version "2.1.0"
}
apply {
    plugin("kotlin")
}

repositories {
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven("https://cache-redirector.jetbrains.com/maven-central")
    intellijPlatform {
        defaultRepositories()
        jetbrainsRuntime()
        snapshots()
    }
}

dependencies {
    intellijPlatform {
        clion("243.21565.87")
        jetbrainsRuntime()
        instrumentationTools()
        bundledPlugins("com.intellij.clion", "com.intellij.cidr.lang","com.intellij.cidr.base", "com.intellij.nativeDebug")
    }
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
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    patchPluginXml {
        sinceBuild.set("243")
        untilBuild.set("243.*")
    }
}