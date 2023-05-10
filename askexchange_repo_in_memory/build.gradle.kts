plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val cache4kVersion: String by project
    val coroutinesVersion: String by project
    val kmpUUIDVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("com.benasher44:uuid:$kmpUUIDVersion")

    implementation(project(":askexchange_common"))
    implementation(project(":askexchange_repo_tests"))

    val ktorVersion: String by project
    val kotestVersion: String by project
    val mockkVersion: String by project
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
