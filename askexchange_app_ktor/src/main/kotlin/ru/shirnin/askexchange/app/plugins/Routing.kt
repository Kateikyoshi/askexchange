package ru.shirnin.askexchange.app.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import ru.shirnin.askexchange.app.AskAppSettings
import ru.shirnin.askexchange.app.v1.v1Answer
import ru.shirnin.askexchange.app.v1.v1Question

fun Application.configureRouting(appSettings: AskAppSettings) {
    install(AutoHeadResponse)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("v1") {
            authenticate("auth-jwt") {
                v1Question(appSettings)
                v1Answer(appSettings)
            }
        }
    }
}

