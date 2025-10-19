plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
}

group = "net.bxx2004"
version = "1.0-SNAPSHOT"
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.2.4")
    implementation("org.apache.commons:commons-crypto:1.2.0")
    implementation("net.lingala.zip4j:zip4j:2.11.5")
    implementation("org.openjdk.nashorn:nashorn-core:15.4")
}


val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}
