plugins {
    application
    id("com.gradleup.shadow") version("8.3.0")
    id("java")
}

application.mainClass = "dev.vxrp.bot.ScpTools" //
group = "dev.vxrp"
version = "1.0-SNAPSHOT"

val jdaVersion = "5.0.0-beta.24" //

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.bspfsystems:yamlconfiguration:2.0.2")
    implementation("org.apache.commons:commons-lang3:3.4")
}

tasks.test {
    useJUnitPlatform()
}