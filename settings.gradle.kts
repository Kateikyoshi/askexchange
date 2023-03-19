rootProject.name = "AskExchange"

//https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_version_management
pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings
        val ktorVersion: String by settings
        val bmuschkoVersion: String by settings

        //https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl
        kotlin("jvm") version kotlinVersion apply false
        id("io.ktor.plugin") version ktorVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
    }
}

//include("module1_nameless")
include("askexchange_api_v1")
include("askexchange_mappers_v1")
include("askexchange_inner_models_v1")
include("askexchange_app_ktor")
findProject(":askexchange_api_v1:untitled")?.name = "untitled"
include("askexchange_stubs")
