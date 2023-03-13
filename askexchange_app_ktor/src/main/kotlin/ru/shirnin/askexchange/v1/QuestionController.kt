package ru.shirnin.askexchange.v1

import InnerQuestionContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.shirnin.askexchange.api.v1.models.*
import toTransportCreate
import toTransportDelete
import toTransportRead
import toTransportUpdate

suspend fun ApplicationCall.createQuestion() {
    val request = receive<QuestionCreateRequest>()
    val context = InnerQuestionContext().apply {
        fromTransport(request)
        questionResponse = InnerQuestionStub.get()
    }
    respond(context.toTransportCreate())
}

suspend fun ApplicationCall.deleteQuestion() {
    val request = receive<QuestionDeleteRequest>()
    val context = InnerQuestionContext().apply {
        fromTransport(request)
        questionResponse = InnerQuestionStub.get()
    }
    respond(context.toTransportDelete())
}

suspend fun ApplicationCall.updateQuestion() {
    val request = receive<QuestionUpdateRequest>()
    val context = InnerQuestionContext().apply {
        fromTransport(request)
        questionResponse = InnerQuestionStub.get()
    }
    respond(context.toTransportUpdate())
}

suspend fun ApplicationCall.readQuestion() {
    val request = receive<QuestionReadRequest>()
    val context = InnerQuestionContext().apply {
        fromTransport(request)
        questionResponse = InnerQuestionStub.get()
    }
    respond(context.toTransportRead())
}