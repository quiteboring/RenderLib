plugins {
  alias(libs.plugins.loom)
  `maven-publish`
  `versioned-catalogues`
}

val modId = providers.gradleProperty("modId").get()
val modVersion = providers.gradleProperty("modVersion").get()

version = "$modVersion+${sc.current.version}"
group = providers.gradleProperty("baseGroup").get()

base {
  archivesName = modId
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
  maven("https://api.modrinth.com/maven")
  maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

loom {
  accessWidenerPath = sc.process(
    rootProject.file("src/main/resources/renderlib.accesswidener"),
    "build/processed.accesswidener"
  )
}

dependencies {
  minecraft(versionedCatalog["minecraft"])

  implementation(libs.fabric.loader)
  implementation(versionedCatalog["fabric-api"])

  runtimeOnly(libs.devauth)
}

tasks {
  processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", versionedCatalog.versions["minecraft"])
    inputs.property("loader_version", libs.versions.fabricLoader.get())

    filesMatching("fabric.mod.json") {
      expand(
        "version" to project.version,
        "loader_version" to libs.versions.fabricLoader.get(),
        "minecraft_version" to versionedCatalog.versions["minecraft"],
      )
    }
  }

  val jar = project.tasks.named("jar")

  register<Copy>("buildAndCollect") {
    group = "build"
    description = "Builds the mod jar and copies it to `build/libs/{mod version}/`"

    inputs.property("version", project.version)
    from(jar)
    into(rootProject.layout.buildDirectory.dir("libs/$modVersion"))
  }
}

tasks.withType<JavaCompile>().configureEach {
  options.release = 25
}

java {
  withSourcesJar()
  sourceCompatibility = JavaVersion.VERSION_25
  targetCompatibility = JavaVersion.VERSION_25
}
