plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val datetimeVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))

    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation(project(":askexchange_common"))
}
