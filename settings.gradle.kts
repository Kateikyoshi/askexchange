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

include("askexchange_api_v1")
include("askexchange_app_business")
include("askexchange_log_api_v1")
include("askexchange_lib_chain_of_resp")
include("askexchange_mappers_v1")
include("askexchange_common")
include("askexchange_app_ktor")
include("askexchange_stubs")
include("askexchange_app_kafka_basic")
include("askexchange_auth")

include("askexchange_logging_common")
include("askexchange_logging_logback")

include("askexchange_repo_postgre")
include("askexchange_repo_in_memory")
include("askexchange_repo_tests")
include("askexchange_repo_stubs")