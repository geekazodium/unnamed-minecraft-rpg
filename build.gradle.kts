repositories{
  flatDir {
    dirs("localLibs")
  }
  maven {
    setUrl("https://repo.dmulloy2.net/repository/public/")
  }
}

plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "1.5.4"
  id("xyz.jpenilla.run-paper") version "1.1.0" // Adds runServer and runMojangMappedServer tasks for testing
  id("com.github.johnrengelman.shadow") version "8.1.0"
}

group = "com.geekazodium"
version = "1.0-SNAPSHOT"
description = "idek"

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
  implementation("org.reflections:reflections:0.10.2")
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")
  compileOnly("com.github.libraryaddict:LibsDisguises:10.0.31")
  compileOnly("com.comphenix.protocol","ProtocolLib","4.7.0")
  // paperweightDevBundle("com.example.paperfork", "1.19.2-R0.1-SNAPSHOT")

  // You will need to manually specify the full dependency if using the groovy gradle dsl
  // (paperDevBundle and paperweightDevBundle functions do not work in groovy)
  // paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.19.2-R0.1-SNAPSHOT")
}

tasks {
  assemble {
    dependsOn(reobfJar)
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    options.release.set(17)
    options.compilerArgs.add("-Xlint:deprecation")
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
  }
  processResources {
    filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
  }

  /*
  reobfJar {
    // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
    // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
    outputJar.set(layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar"))
  }
   */
}
