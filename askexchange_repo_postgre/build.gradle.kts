plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val coroutinesVersion: String by project
    val kmpUUIDVersion: String by project
    val exposedVersion: String by project
    val postgresDriverVersion: String by project

    implementation("org.postgresql:postgresql:42.6.0")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")

    implementation(kotlin("stdlib-jdk8"))
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
    withType<Test> {
        environment("askexchange.sql_drop_db", true)
        environment("askexchange.sql_fast_migration", true)
    }
}
