package ru.shirnin.askexchange.app

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.app.conf.KtorAuthConfig

class AnswersTest : FunSpec({
    test("answer create") {
        testApplication {
            application {
                module(
                    appSettings = initTestAppSettings(),
                    authSettings = KtorAuthConfig.TEST
                )
            }
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        enable(SerializationFeature.INDENT_OUTPUT)
                        writerWithDefaultPrettyPrinter()
                    }
                }
            }
            val response = client.post("/v1/answer/create") {
                val requestObj = AnswerCreateRequest(
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    ),
                    requestType = "create",
                    debugId = "11",
                    answerCreateObject = AnswerCreateObject(
                        userId = "001",
                        questionId = "002",
                        answer = Answer(
                            body = "I reply for likes only",
                            date = "2023-03-17T20:17:24.00Z",
                            likes = 34
                        )
                    )
                )
                contentType(ContentType.Application.Json)
                setBody(requestObj)
                addAuth()
            }
            val responseObj = response.body<AnswerCreateResponse>()

            response.status.value shouldBe 200
            responseObj.answerId shouldBe "999"
        }
    }
    test("answer update") {
        testApplication {
            application {
                module(
                    appSettings = initTestAppSettings(),
                    authSettings = KtorAuthConfig.TEST
                )
            }
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        enable(SerializationFeature.INDENT_OUTPUT)
                        writerWithDefaultPrettyPrinter()
                    }
                }
            }
            val response = client.post("/v1/answer/update") {
                val requestObj = AnswerUpdateRequest(
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    ),
                    requestType = "update",
                    debugId = "11",
                    answerUpdateObject = AnswerUpdateObject(
                        answerId = "100",
                        answer = Answer(
                            body = "I reply for likes only",
                            date = "2023-03-17T20:17:24.00Z",
                            likes = 34
                        )
                    )
                )
                contentType(ContentType.Application.Json)
                setBody(requestObj)
                addAuth()
            }
            val responseObj = response.body<AnswerUpdateResponse>()

            response.status.value shouldBe 200
            responseObj.answerId shouldBe "999"
        }
    }
})