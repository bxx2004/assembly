plugins {
    kotlin("jvm") version "2.3.0"
}

group = "net.bxx2004"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("http://mavenrepo.revoist.cn")
        isAllowInsecureProtocol = true
    }

}
subprojects {
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("http://mavenrepo.revoist.cn")
            isAllowInsecureProtocol = true
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}