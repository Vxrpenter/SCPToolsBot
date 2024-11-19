plugins {
    application
    java
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("com.gradleup.shadow") version("8.3.3")
}


application.mainClass = "dev.vxrp.MainKt"
group = "dev.vxrp"
version= "0.3.1"

val jdaVersion = "5.2.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.charleskorn.kaml:kaml:0.65.0")
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.bspfsystems:yamlconfiguration:2.0.2")
    implementation("org.apache.commons:commons-lang3:3.4")
    implementation("com.google.code.gson:gson:2.11.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

val createVersionProperties by tasks.registering(WriteProperties::class) {
    val filePath = sourceSets.main.map {
        it.output.resourcesDir!!.resolve("dev/vxrp/version.properties")
    }
    destinationFile = filePath

    property("version", project.version.toString())
}

