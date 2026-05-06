plugins {
    application
    kotlin("jvm") version "2.3.20"
    kotlin("plugin.serialization") version "2.3.20"
    id("com.gradleup.shadow") version("9.4.1")
    id("io.gitlab.arturbosch.detekt").version("1.23.8")
}

application.mainClass = "dev.vxrp.MainKt"
group = "dev.vxrp"
version= "1.1.2"

kotlin {
    jvmToolchain(22)
}

repositories {
    mavenCentral()
}

val ktorVersion = "3.4.2"
val kotlinxCoroutinesVersion = "1.10.2"

val exposedVersion = "1.2.0"
val sqliteVersion = "3.51.3.0"
val mySqlVersion = "9.6.0"
val postgresSqlVersion = "42.7.10"
val mariaDBVersion = "3.5.8"

val kotlinxSerializationVersion = "1.10.0"
val kamlVersion = "0.104.0"
val jdaVersion = "5.6.1"
val jdaKtxVersion = "0.12.0"
val secretLabKotlinVersion = "0.4.3"
val updaterVersion = "0.1.1"
val configLiteVersion = "0.1.1"
val logbackVersion = "1.5.32"
val gsonVersion = "2.13.2"

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
    implementation("io.github.vxrpenter:config-lite:$configLiteVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("com.charleskorn.kaml:kaml:$kamlVersion")

    // Discord api implementation + Logback
    implementation("net.dv8tion:JDA:$jdaVersion") {
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("club.minnced:jda-ktx:$jdaKtxVersion")

    // Api Wrapper
    implementation("io.github.vxrpenter:updater:$updaterVersion")
    implementation("io.github.vxrpenter:secretlab-kotlin:$secretLabKotlinVersion")

    // String and JSON tooling
    implementation("com.google.code.gson:gson:$gsonVersion")
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

