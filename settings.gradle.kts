rootProject.name = "AskExchange"

//https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_version_management
pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings

        //https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl
        kotlin("jvm") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("module1_nameless")
include("askExchangeApi_v1")
include("askExchangeMappers_v1")
include("askExchangeInnerModels_v1")