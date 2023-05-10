plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val coroutinesVersion: String by project
    val mockkVersion: String by project
    val ktorVersion: String by project

    implementation(project(":askexchange_common"))
    implementation(project(":askexchange_stubs"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation(project(":askexchange_repo_tests"))
}