import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    //java
    //application
    kotlin("jvm")
}

//application {
//    mainClass.set("ru.shirnin.askexchange.MainKt")
//}

group = "ru.shirnin.askexchange"
version = "1.0.2"


allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

//tasks.jar {
//    manifest {
//        attributes["Main-Class"] = "ru.shirnin.askexchange.MainKt"
//    }
//    configurations["compileClasspath"].forEach { file: File ->
//        from(zipTree(file.absoluteFile))
//    }
//    duplicatesStrategy = DuplicatesStrategy.INCLUDE
//}

subprojects {
    group = rootProject.group
    version = rootProject.version

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}