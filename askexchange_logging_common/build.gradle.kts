val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

//turns out there is a native ktor plugin for docker, so this can be left out for now
//    id("com.bmuschko.docker-java-application")
//    id("com.bmuschko.docker-remote-api")
}

group = "ru.shirnin.askexchange"
version = "0.0.1"

repositories {
    mavenCentral()
}


tasks {

    //otherwise there will be an error when running Kotest tests
    test {
        useJUnitPlatform()
    }

}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")


    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    //transport models
    implementation(project(":askexchange_mappers_v1"))
    implementation(project(":askexchange_api_v1"))
    implementation(project(":askexchange_inner_models_v1"))

    //stubs
    implementation(project(":askexchange_stubs"))

    //tests
    val kotestVersion: String by project
    val mockkVersion: String by project
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
}

