package ru.shirnin.askexchange.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.application.*
import ru.shirnin.askexchange.v1.v1Answer
import ru.shirnin.askexchange.v1.v1Question

fun Application.configureRouting() {
    install(AutoHeadResponse)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("v1") {
            v1Question()
            v1Answer()
        }
    }
}

