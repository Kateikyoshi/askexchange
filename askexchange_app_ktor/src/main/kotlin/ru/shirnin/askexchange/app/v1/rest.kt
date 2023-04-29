package ru.shirnin.askexchange.app.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.shirnin.askexchange.app.AskAppSettings

fun Route.v1Question(appSettings: AskAppSettings) {
    val loggerQuestion = appSettings.chainSettings.loggerProvider.logger(Route::v1Question::class)
    route("question") {
        post("create") {
            call.createQuestion(appSettings, loggerQuestion)
        }
        post("delete") {
            call.deleteQuestion(appSettings, loggerQuestion)
        }
        post("update") {
            call.updateQuestion(appSettings, loggerQuestion)
        }
        post("read") {
            call.readQuestion(appSettings, loggerQuestion)
        }
    }
}

fun Route.v1Answer(appSettings: AskAppSettings) {
    val loggerAnswer = appSettings.chainSettings.loggerProvider.logger(Route::v1Answer::class)
    route("answer") {
        post("create") {
            call.createAnswer(appSettings, loggerAnswer)
        }
        post("delete") {
            call.deleteAnswer(appSettings, loggerAnswer)
        }
        post("update") {
            call.updateAnswer(appSettings, loggerAnswer)
        }
        post("read") {
            call.readAnswer(appSettings, loggerAnswer)
        }
    }
}