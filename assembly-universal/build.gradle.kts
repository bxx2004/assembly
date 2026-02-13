plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
}

group = "net.bxx2004"
version = "lastest"
repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.2.4")
    implementation("org.apache.commons:commons-crypto:1.2.0")
    implementation("net.lingala.zip4j:zip4j:2.11.5")
    implementation("org.openjdk.nashorn:nashorn-core:15.4")
    implementation("com.madgag:animated-gif-lib:1.4")
    implementation("com.github.goxr3plus:java-stream-player:10.0.2")
    implementation("org.appliedenergistics.yoga:yoga:1.0.0")
    implementation("com.typesafe:config:1.4.5")
}


val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}
