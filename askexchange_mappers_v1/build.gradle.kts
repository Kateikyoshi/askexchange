plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {

    val kotestVersion: String by project
    val mockkVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":askexchange_api_v1"))
    implementation(project(":askexchange_inner_models_v1"))

    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
}

tasks {
    //otherwise there will be an error when running Kotest tests
    test {
        useJUnitPlatform()
    }
}