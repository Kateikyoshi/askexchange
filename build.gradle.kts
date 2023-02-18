import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    kotlin("jvm") apply false
}

application {
    mainClass.set("ru.shirnin.askexchange.MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.shirnin.askexchange.MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

subprojects {
    group = "ru.shirnin.askexchange"
    version = "1.0.1"

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}