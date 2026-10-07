pluginManagement {
  repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://maven.kikugie.dev/releases")
    maven("https://maven.kikugie.dev/snapshots")
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

rootProject.name = "RenderLib"

dependencyResolutionManagement {
  versionCatalogs {
    versions.forEach { version ->
      val versionName = version.replace('.', '_')
      create("libs${versionName.replace("_", "")}") {
        from(
          files(
            rootProject.projectDir.resolve("gradle/$versionName.versions.toml")
          )
        )
      }
    }
  }
}
