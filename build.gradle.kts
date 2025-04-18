plugins {
    application
    java
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("com.gradleup.shadow") version("8.3.3")
    id("io.gitlab.arturbosch.detekt").version("1.23.7")
}


application.mainClass = "dev.vxrp.MainKt"
group = "dev.vxrp"
version= "1.1.0-alpha2"


repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

kotlin {
    jvmToolchain(22)
}

val ktorVersion = "3.1.2"
val kotlinxCoroutinesVersion = "1.9.0"

val exposedVersion = "0.56.0"
val sqliteVersion = "3.45.1.0"
val mySqlVersion = "9.2.0"
val postgresSqlVersion = "42.7.2"
val mariaDBVersion = "3.5.0"

val kotlinxSerializationVersion = "1.7.3"
val kamlVersion = "0.65.0"
val jdaVersion = "5.2.1"
val jdaKtxVersion = "0.12.0"
val logbackVersion = "1.5.6"
val apacheCommonsVersion = "3.4"
val gsonVersion = "2.11.0"

dependencies {
    // Default
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")

    // Database tools and drivers
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.xerial:sqlite-jdbc:$sqliteVersion")
    implementation("com.mysql:mysql-connector-j:$mySqlVersion")
    implementation("org.postgresql:postgresql:$postgresSqlVersion")
    implementation("org.mariadb.jdbc:mariadb-java-client:$mariaDBVersion")

    // Config and Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("com.charleskorn.kaml:kaml:$kamlVersion")

    // Discord api implementation + Logback
    implementation("net.dv8tion:JDA:$jdaVersion") {
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("club.minnced:jda-ktx:$jdaKtxVersion")

    // String and JSON tooling
    implementation("org.apache.commons:commons-lang3:$apacheCommonsVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")

    // Ascii
    implementation("com.jakewharton.picnic:picnic:0.7.0")

    // Test
    testImplementation(kotlin("test"))

    // DEPRECATED
    implementation("org.bspfsystems:yamlconfiguration:2.0.2")
}

val createVersionProperties by tasks.registering(WriteProperties::class) {
    val filePath = sourceSets.main.map {
        it.output.resourcesDir!!.resolve("dev/vxrp/version.properties")
    }
    destinationFile = filePath

    property("version", project.version.toString())
}

tasks.classes {
    dependsOn(createVersionProperties)
}

tasks.test {
    useJUnitPlatform()
}

