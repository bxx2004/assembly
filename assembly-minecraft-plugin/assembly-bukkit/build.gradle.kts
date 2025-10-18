
import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    java
    id("io.izzel.taboolib") version "2.0.27"
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
}

taboolib {
    env {
        install(Basic)
        install(BukkitUtil)
        install(CommandHelper)
        install(Bukkit)
        install(Kether)
        install(BukkitFakeOp)
    }
    description {
        name = "assembly-bukkit"
        contributors {
            name("bxx2004")
        }
    }
    version {
        taboolib = "6.2.3-1a8d7125"
        //skipKotlinRelocate = true
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://mvn.devos.one/releases")
    }
}

dependencies {
    compileOnly("ink.ptms.core:v12105:12105:mapped")
    compileOnly("ink.ptms.core:v12105:12105:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    taboo(project(":assembly-universal")){
        exclude(module = "org.jetbrains.kotlin")
        exclude(module = "org.objectweb")
        isTransitive = false
    }
    taboo("com.google.code.gson:gson:2.2.4")
    taboo("org.apache.commons:commons-crypto:1.2.0")
    taboo("net.lingala.zip4j:zip4j:2.11.5")
    taboo("org.openjdk.nashorn:nashorn-core:15.4")
    compileOnly("io.netty:netty-all:5.0.0.Alpha2")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}
kotlin {
    jvmToolchain(22)
}