plugins {
    application
    id("com.gradleup.shadow") version("8.3.3")
    id("java")
}


application.mainClass = "dev.vxrp.bot.ScpTools" //
group = "dev.vxrp"
version= "0.2.2"

val jdaVersion = "5.0.0-beta.24" //

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.bspfsystems:yamlconfiguration:2.0.2")
    implementation("org.apache.commons:commons-lang3:3.4")
    implementation("com.google.code.gson:gson:2.11.0")
}

val createVersionProperties by tasks.registering(WriteProperties::class) {
    val filePath = sourceSets.main.map {
        it.output.resourcesDir!!.resolve("vxrp/dev/version.properties")
    }
    destinationFile = filePath

    property("version", project.version.toString())
}
tasks.classes {
    dependsOn(createVersionProperties)
}