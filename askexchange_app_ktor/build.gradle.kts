import io.ktor.plugin.features.*

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("io.ktor.plugin")

//turns out there is a native ktor plugin for docker, so this can be left out for now
//    id("com.bmuschko.docker-java-application")
//    id("com.bmuschko.docker-remote-api")
}

group = "ru.shirnin.askexchange"
version = "0.0.1"
application {
    mainClass.set("ru.shirnin.askexchange.ApplicationKt")

//    val isDevelopment: Boolean = project.ext.has("development")
//    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

ktor {
    docker {
        jreVersion.set(JreVersion.valueOf("JRE_$javaVersion"))
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
    }
}

tasks {

    //otherwise there will be an error when running Kotest tests
    test {
        useJUnitPlatform()
    }
}

dependencies {
    val ktorVersion: String by project
    val logbackVersion: String by project
    val logbackMoreAppendersVersion: String by project
    val fluentLoggerVersion: String by project
    val serializationVersion: String by project

    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-caching-headers-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auto-head-response-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-config-yaml-jvm:$ktorVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.sndyuk:logback-more-appenders:$logbackMoreAppendersVersion")
    implementation("org.fluentd:fluent-logger:$fluentLoggerVersion")

    //transport models
    implementation(project(":askexchange_mappers_v1"))
    implementation(project(":askexchange_api_v1"))
    implementation(project(":askexchange_log_api_v1"))
    implementation(project(":askexchange_common"))
    implementation(project(":askexchange_logging_common"))
    implementation(project(":askexchange_logging_logback"))

    implementation(project(":askexchange_lib_chain_of_resp"))
    implementation(project(":askexchange_app_business"))

    implementation(project(":askexchange_repo_postgre"))
    implementation(project(":askexchange_repo_in_memory"))
    implementation(project(":askexchange_repo_stubs"))

    //stubs
    implementation(project(":askexchange_stubs"))


    //tests
    val kotestVersion: String by project
    val mockkVersion: String by project
    val kotestTestContainersExtensionVersion: String by project
    val testcontainersVersion: String by project

    testImplementation(project(":askexchange_repo_tests"))
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:$kotestTestContainersExtensionVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")

}

