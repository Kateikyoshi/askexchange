plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val datetimeVersion: String by project

    implementation(kotlin("stdlib"))

    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
}
