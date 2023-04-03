plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val ktorVersion: String by project
    val logbackVersion: String by project
    val serializationVersion: String by project
    val logbackEncoderVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":askexchange_logging_common"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")


    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")


    //tests
    val kotestVersion: String by project
    val mockkVersion: String by project
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
}

