
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
        isTransitive = false
    }
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