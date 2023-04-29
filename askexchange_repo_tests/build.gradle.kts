plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val coroutinesVersion: String by project
    val kotestVersion: String by project
    val mockkVersion: String by project
    val ktorVersion: String by project

    implementation(project(":askexchange_common"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    api("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    api("io.mockk:mockk:$mockkVersion")
    api("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    api("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    //api(kotlin("test-junit"))

    //tests


}
