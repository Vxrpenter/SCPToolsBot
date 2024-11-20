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


repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

val exposedVersion = "0.56.0"
val sqliteVersion = "3.45.1.0"
val mySqlVersion = "9.1.0"
val postgresSqlVersion = "42.7.2"
val mariaDBVersion = "3.5.0"

val kotlinVersion = "1.7.3"
val kamlVersion = "0.65.0"
val jdaVersion = "5.2.1"
val logbackVersion = "1.5.6"
val apacheCommonsVersion = "3.4"
val gsonVersion = "2.11.0"

dependencies {
    // Database tools and drivers
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.xerial:sqlite-jdbc:$sqliteVersion")
    implementation("com.mysql:mysql-connector-j:$mySqlVersion")
    implementation("org.postgresql:postgresql:$postgresSqlVersion")
    implementation("org.mariadb.jdbc:mariadb-java-client:$mariaDBVersion")

    // Config and Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinVersion")
    implementation("com.charleskorn.kaml:kaml:$kamlVersion")

    // Discord api implementation + Logback
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // String and JSON tooling
    implementation("org.apache.commons:commons-lang3:$apacheCommonsVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")

    // OLD
    implementation("org.bspfsystems:yamlconfiguration:2.0.2")
}

val createVersionProperties by tasks.registering(WriteProperties::class) {
    val filePath = sourceSets.main.map {
        it.output.resourcesDir!!.resolve("dev/vxrp/version.properties")
    }
    destinationFile = filePath

    property("version", project.version.toString())
}

