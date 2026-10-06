pluginManagement {
  repositories {
    maven("https://maven.fabricmc.net/")
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins {
  id("dev.kikugie.stonecutter") version "0.9.8"
}

val versions = listOf("26.3", "26.2")

stonecutter {
  create(rootProject) {
    versions(versions)
    vcsVersion = versions.first()
  }
}

rootProject.name = providers.gradleProperty("modName").get()
