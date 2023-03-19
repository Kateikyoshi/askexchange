package ru.shirnin.askexchange.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1Question() {
    route("question") {
        post("create") {
            call.createQuestion()
        }
        post("delete") {
            call.deleteQuestion()
        }
        post("update") {
            call.updateQuestion()
        }
        post("read") {
            call.readQuestion()
        }
    }
}

fun Route.v1Answer() {
    route("answer") {
        post("create") {
            call.createAnswer()
        }
        post("delete") {
            call.deleteAnswer()
        }
        post("update") {
            call.updateAnswer()
        }
        post("read") {
            call.readAnswer()
        }
    }
}