import dev.detekt.gradle.extensions.FailOnSeverity
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.loom)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.detekt)
  `maven-publish`
}

val baseGroup = providers.gradleProperty("baseGroup").get()
val modId = providers.gradleProperty("modId").get()
val modName = providers.gradleProperty("modName").get()
val modVersion = providers.gradleProperty("modVersion").get()

version = modVersion
group = baseGroup

base {
  archivesName = modName
}

detekt {
  buildUponDefaultConfig = true
  config.setFrom(rootProject.file("config/detekt/detekt.yml"))
  failOnSeverity = FailOnSeverity.Never
  ignoredBuildTypes = listOf()
  allRules = false
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
    }
  }
}

repositories {
  mavenCentral()
  maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

loom {
  accessWidenerPath = rootProject.file("src/main/resources/${modId}.accesswidener")
}

val jij = configurations.create("jij")

jij.excludeProvidedLibs()

dependencies {
  minecraft(libs.minecraft)

  api(libs.fabric.loader)
  api(libs.fabric.api)
  api(libs.fabric.kotlin)

  runtimeOnly("me.djtheredstoner:DevAuth-fabric:1.2.2")
}

addResolvedDependencies(jij, "compileOnly", "include", "api")

tasks {
  processResources {
    val resourceProperties = mapOf(
      "fabricLoaderVersion" to libs.versions.fabric.loader.get(),
      "fabricKotlinVersion" to libs.versions.fabric.kotlin.get(),
      "minecraftVersion" to libs.versions.minecraft.version.get(),
      "modId" to modId,
      "modName" to modName,
      "modVersion" to modVersion,
      "baseGroup" to baseGroup,
    )

    inputs.properties(resourceProperties)

    filesMatching(listOf("fabric.mod.json", "$modId.mixins.json")) {
      expand(resourceProperties)
    }
  }
}

tasks.withType<JavaCompile>().configureEach {
  options.release = 25
}

kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_25
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_25
  targetCompatibility = JavaVersion.VERSION_25
}
