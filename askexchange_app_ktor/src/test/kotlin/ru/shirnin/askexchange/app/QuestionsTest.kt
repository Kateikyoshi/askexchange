package ru.shirnin.askexchange.app

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.shirnin.askexchange.api.v1.models.*

class QuestionsTest : FunSpec({
    test("question create") {
        testApplication {
            application {
                module(
                    appSettings = initTestAppSettings()
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
            val response = client.post("/v1/question/create") {
                val requestObj = QuestionCreateRequest(
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    ),
                    requestType = "create",
                    debugId = "11",
                    questionCreateObject = QuestionCreateObject(
                        userId = "245",
                        question = Question(
                            title = "Kotlin is what?",
                            body = "I am not sure what to ask"
                        )
                    )
                )
                contentType(ContentType.Application.Json)
                setBody(requestObj)
            }
            val responseObj = response.body<QuestionCreateResponse>()

            println("resp obj\n$responseObj")

            response.status.value shouldBe 200
            responseObj.questionId shouldBe "1"
        }
    }
    test("question update") {
        testApplication {
            application {
                module(
                    appSettings = initTestAppSettings()
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
            val response = client.post("/v1/question/update") {
                val requestObj = QuestionUpdateRequest(
                    debug = Debug(
                        mode = RequestDebugMode.STUB,
                        stub = RequestDebugStubs.SUCCESS
                    ),
                    requestType = "update",
                    debugId = "11",
                    questionUpdateObject = QuestionUpdateObject(
                        question = Question(
                            title = "Kotlin is what?",
                            body = "I am not sure what to ask"
                        ),
                        questionId = "999"
                    )
                )
                contentType(ContentType.Application.Json)
                setBody(requestObj)
            }
            val responseObj = response.body<QuestionUpdateResponse>()

            println(responseObj)

            response.status.value shouldBe 200
            responseObj.questionId shouldBe "999"
        }
    }
})