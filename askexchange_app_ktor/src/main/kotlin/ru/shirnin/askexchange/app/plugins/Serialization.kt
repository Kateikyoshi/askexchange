package ru.shirnin.askexchange.app.plugins

import apiV1Mapper
import io.ktor.serialization.jackson.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }
//    routing {
//        get("/json/kotlinx-serialization") {
//                call.respond(mapOf("hello" to "world"))
//            }
//    }
}
