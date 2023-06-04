package ru.shirnin.askexchange.app.v1

import fromTransport
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.shirnin.askexchange.api.v1.models.IQuestionRequest
import ru.shirnin.askexchange.api.v1.models.IQuestionResponse
import ru.shirnin.askexchange.app.AskAppSettings
import ru.shirnin.askexchange.app.conf.toModel
import ru.shirnin.askexchange.app.toLog
import ru.shirnin.askexchange.inner.models.InnerCommand
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.inner.models.helpers.asInnerError
import ru.shirnin.askexchange.logging.common.AskLogWrapper
import toTransport

suspend inline fun <reified Q : IQuestionRequest, @Suppress("unused") reified R : IQuestionResponse> ApplicationCall.processQuestionV1(
    appSettings: AskAppSettings,
    log: AskLogWrapper,
    logId: String,
    command: InnerCommand? = null,
) {
    val processor = appSettings.questionProcessor
    val context = InnerQuestionContext(
        settings = appSettings.chainSettings
    )

    try {
        log.doWithLogging(logId) {
            context.principal = principal<JWTPrincipal>().toModel()

            val request = receive<Q>()

            context.fromTransport(request)

            log.info(
                msg = "${context.command} request was received",
                data = context.toLog("${logId}-request")
            )

            processor.exec(context)

            log.info(
                msg = "${context.command} request was sent",
                data = context.toLog("${logId}-request")
            )

            respond(context.toTransport())
        }
    } catch (e: Throwable) {
        log.doWithLogging(id = "${logId}-failure") {
            command?.also { context.command = it }
            log.error(
                msg = "$command handling failed",
            )
            context.state = InnerState.FAILED
            context.errors.add(e.asInnerError())
            processor.exec(context)
            respond(context.toTransport())
        }
    }
}