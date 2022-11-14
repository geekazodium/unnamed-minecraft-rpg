//import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

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
  id("io.papermc.paperweight.userdev") version "1.3.8"
  id("xyz.jpenilla.run-paper") version "1.1.0" // Adds runServer and runMojangMappedServer tasks for testing
  //id("net.minecrell.plugin-yml.bukkit") version "0.5.2" // Generates plugin.yml
}

group = "com.geekazodium"
version = "1.0-SNAPSHOT"
description = "idek"

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
  paperDevBundle("1.18.2-R0.1-SNAPSHOT")
  implementation("com.github.libraryaddict:LibsDisguises:10.0.31")
  compileOnly("com.comphenix.protocol","ProtocolLib","4.7.0")
  // paperweightDevBundle("com.example.paperfork", "1.19.2-R0.1-SNAPSHOT")

  // You will need to manually specify the full dependency if using the groovy gradle dsl
  // (paperDevBundle and paperweightDevBundle functions do not work in groovy)
  // paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.19.2-R0.1-SNAPSHOT")
}

tasks {
  // Configure reobfJar to run when invoking the build task
  assemble {
    dependsOn(reobfJar)
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

    // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
    // See https://openjdk.java.net/jeps/247 for more information.
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
/*
bukkit {
  load = BukkitPluginDescription.PluginLoadOrder.STARTUP
  main = "com.geekazodium.cavernsofamethyst.Main"
  apiVersion = "1.18"
  authors = listOf("Geekazodium")
}
*/
