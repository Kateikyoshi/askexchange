val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project

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

//    val dockerJvmDockerfile by creating(com.bmuschko.gradle.docker.tasks.image.Dockerfile::class) {
//        group = "docker"
//        from("bellsoft/liberica-openjdk-alpine:17")
//        copyFile("ktor.jar", "app/ktor.jar")
//        exposePort(8080)
//        entryPoint("java", "-Xms256m", "-Xmx512m", "-jar", "app/ktor.jar")
//    }
//    create("dockerBuildJvmImage", com.bmuschko.gradle.docker.tasks.image.DockerBuildImage::class) {
//        group = "docker"
//        dependsOn(dockerJvmDockerfile, named("build"))
//        doFirst {
//            copy {
//                from("${project.buildDir}/libs/askexchange_app_ktor-0.0.1.jar")
//                into("${project.buildDir}/docker/ktor.jar")
//            }
//        }
//        images.add("${project.name}:${project.version}")
//    }
}

dependencies {
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

