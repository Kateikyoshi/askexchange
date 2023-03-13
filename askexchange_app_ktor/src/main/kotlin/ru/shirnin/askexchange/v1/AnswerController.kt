package ru.shirnin.askexchange.v1

import InnerAnswerContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.shirnin.askexchange.api.v1.models.AnswerCreateRequest
import ru.shirnin.askexchange.api.v1.models.AnswerDeleteRequest
import ru.shirnin.askexchange.api.v1.models.AnswerReadRequest
import ru.shirnin.askexchange.api.v1.models.AnswerUpdateRequest
import toTransportCreate
import toTransportDelete
import toTransportRead
import toTransportUpdate

suspend fun ApplicationCall.createAnswer() {
    val request = receive<AnswerCreateRequest>()
    val context = InnerAnswerContext().apply {
        fromTransport(request)
        answerResponse = InnerAnswerStub.get()
    }
    respond(context.toTransportCreate())
}

suspend fun ApplicationCall.deleteAnswer() {
    val request = receive<AnswerDeleteRequest>()
    val context = InnerAnswerContext().apply {
        fromTransport(request)
        answerResponse = InnerAnswerStub.get()
    }
    respond(context.toTransportDelete())
}

suspend fun ApplicationCall.updateAnswer() {
    val request = receive<AnswerUpdateRequest>()
    val context = InnerAnswerContext().apply {
        fromTransport(request)
        answerResponse = InnerAnswerStub.get()
    }
    respond(context.toTransportUpdate())
}

suspend fun ApplicationCall.readAnswer() {
    val request = receive<AnswerReadRequest>()
    val context = InnerAnswerContext().apply {
        fromTransport(request)
        answerResponse = InnerAnswerStub.get()
    }
    respond(context.toTransportRead())
}