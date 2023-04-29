plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("io.ktor.plugin")
}

group = "ru.shirnin.askexchange"
version = "0.0.1"
application {
    mainClass.set("ru.shirnin.askexchange.ApplicationKt")
}

repositories {
    mavenCentral()
}

ktor {
    docker {
        jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_17)
        localImageName.set("bellsoft/liberica-openjdk-alpine")
        imageTag.set("17")
        portMappings.set(listOf(
            io.ktor.plugin.features.DockerPortMapping(
                80,
                8080,
                io.ktor.plugin.features.DockerPortMappingProtocol.TCP
            )
        ))
    }
}

tasks {
    //otherwise there will be an error when running Kotest tests
    test {
        useJUnitPlatform()
    }
}

dependencies {
    val coroutinesVersion: String by project
    val ktorVersion: String by project
    val kotlinVersion: String by project
    val logbackVersion: String by project
    val serializationVersion: String by project
    val kafkaVersion: String by project
    val atomicfuVersion: String by project

    implementation(kotlin("stdlib"))

    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")
    implementation("io.github.microutils:kotlin-logging:3.0.5")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    //transport models
    implementation(project(":askexchange_mappers_v1"))
    implementation(project(":askexchange_api_v1"))
    implementation(project(":askexchange_common"))

    //stubs
    implementation(project(":askexchange_stubs"))

    //tests
    val kotestVersion: String by project
    val mockkVersion: String by project
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
}

