import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
    id("com.gradleup.shadow") version("8.3.3")
    id("io.gitlab.arturbosch.detekt").version("1.23.7")
}


application.mainClass = "dev.vxrp.MainKt"
group = "dev.vxrp"
version= "1.1.1"

kotlin {
    jvmToolchain(22)
}

repositories {
    mavenCentral()
}

val ktorVersion = "3.2.0"
val kotlinxCoroutinesVersion = "1.10.2"

val exposedVersion = "0.61.0"
val sqliteVersion = "3.50.1.0"
val mySqlVersion = "9.3.0"
val postgresSqlVersion = "42.7.7"
val mariaDBVersion = "3.5.3"

val configLiteVersion = "0.1.1"
val updaterVersion = "0.1.0-rc3"
val jdaVersion = "5.6.1"
val botCommandsVersion = "3.0.0-beta.3"
val secretLabKotlinVersion = "0.4.3"
val kotlinLoggingVersion = "7.0.3"
val logbackVersion = "1.5.20"

val stacktraceDecoroutinatorVersion = "2.5.6"

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

    // Config and Updates
    implementation("io.github.vxrpenter:config-lite:$configLiteVersion")
    implementation("io.github.vxrpenter:updater:$updaterVersion")

    // Discord api implementation + Logback
    implementation("net.dv8tion:JDA:$jdaVersion") {
        exclude(module = "opus-java")
    }
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.freya022:BotCommands:$botCommandsVersion")

    // Api Wrapper
    implementation("io.github.vxrpenter:secretlab-kotlin:$secretLabKotlinVersion")

    // Stacktraces
    implementation("dev.reformator.stacktracedecoroutinator:stacktrace-decoroutinator-jvm:$stacktraceDecoroutinatorVersion")
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

tasks.withType<ShadowJar> {
    mergeServiceFiles()
    archiveFileName.set("SCP_Tools.jar")
}

