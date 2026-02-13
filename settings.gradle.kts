pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}
rootProject.name = "assembly"
include("assembly-universal")
include("assembly-minecraft")
include("assembly-minecraft-mod:neoforge-1211")
include("assembly-minecraft-plugin:assembly-bukkit")