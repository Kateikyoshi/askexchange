package ru.shirnin.askexchange.app.integration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.JdbcTestContainerExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.testcontainers.containers.PostgreSQLContainer
import ru.shirnin.askexchange.api.v1.models.*
import ru.shirnin.askexchange.app.*


class QuestionsIntegrationTest : FunSpec({

    //https://kotest.io/docs/extensions/test_containers.html

    val postgresContainer = PostgreSQLContainer<Nothing>("postgres:15.2-alpine").apply {
        startupAttempts = 1
        //withInitScript("init.sql")
        withDatabaseName("askexchange")
        withUsername("postgres")
        withPassword("postgres")
        withExposedPorts(5432)

        //withUrlParam("connectionTimeZone", "Z")
        //withUrlParam("zeroDateTimeBehavior", "convertToNull")
    }

    //postgresContainer.start() isn't needed, extension will do it for you
    val dataSource = install(JdbcTestContainerExtension(postgresContainer)) {
        poolName = "myconnectionpool"
        maximumPoolSize = 8
        idleTimeout = 10000
        //dbInitScripts = listOf("/init.sql", "/sql-changesets")
    }

    val exposedPort = postgresContainer.firstMappedPort

    test("question create prod with test container") {

        testApplication {

            application {
                module(
                    appSettings = initIntegrationTestAppSettings(
                        dbUrl = "jdbc:postgresql://localhost:$exposedPort/askexchange"
                    )
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
                        mode = RequestDebugMode.PROD
                    ),
                    requestType = "create",
                    debugId = "11",
                    questionCreateObject = QuestionCreateObject(
                        userId = "55665",
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

            response.status.value shouldBe 200
            responseObj.questionId shouldNotBe ""
        }
    }
})