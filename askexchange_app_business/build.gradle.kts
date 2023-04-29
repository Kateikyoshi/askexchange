

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

//group = "ru.shirnin.askexchange"
//version = "0.0.1"


tasks {
    test {
        useJUnitPlatform()
    }
}

dependencies {
    val ktorVersion: String by project
    val kotlinVersion: String by project
    val logbackVersion: String by project
    val logbackMoreAppendersVersion: String by project
    val fluentLoggerVersion: String by project
    val serializationVersion: String by project


    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")


    //stubs
    implementation(project(":askexchange_stubs"))
    implementation(project(":askexchange_inner_models_v1"))
    implementation(project(":askexchange_lib_chain_of_resp"))

    //tests
    val kotestVersion: String by project
    val mockkVersion: String by project
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
}
