plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val datetimeVersion: String by project

    implementation(kotlin("stdlib"))

    //api(project(":ok-marketplace-lib-logging-common"))
    api(project(":askexchange_logging_common"))

    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
}
