plugins {
    kotlin("jvm") version "2.2.0"
}

group = "net.bxx2004"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.2.4")
}


val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}
