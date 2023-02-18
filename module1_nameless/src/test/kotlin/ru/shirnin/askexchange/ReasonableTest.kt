package ru.shirnin.askexchange

import io.kotest.core.spec.style.FunSpec
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.json.Json

/**
 * This test tries to delve into TDD
 *
 */
class TDDTest: FunSpec({

    test("User creation test with RegistrationHandler") {

        val registerPayload = """
            {
                "login": "login",
                "password": "password",
                "email": "email",
                "expertise": "expertise"
            }
        """.trimIndent()

        val user = Json.decodeFromString<User>(string = registerPayload)
        val databaseSaverMock = mockk<DatabaseSaver>()
        val registrationHandler = RegistrationHandler(databaseSaverMock)

        registrationHandler.register(registerPayload)
        verify(exactly = 1) { databaseSaverMock.save(user) }
    }

    test("Question posting test with QuestionPostHandler") {

        val questionPostPayload = """
            {
                "questionTitle": "questionTitle",
                "questionBody": "questionBody"
            }
        """.trimIndent()

        val question = Json.decodeFromString<Question>(string = questionPostPayload)
        question.user = "user" //login is taken from HttpHeaders because login is required to post a question
        val databaseSaverMock = mockk<DatabaseSaver>()
        val questionPostHandler = QuestionPostHandler(databaseSaverMock)

        questionPostHandler.ask(questionPostPayload)
        verify(exactly = 1) { databaseSaverMock.save(question) }
    }

    test("Answer posting test with AnswerPostHandler") {

        val questionPostPayload = """
            {
                "answerBody": "answerBody",
                "questionTitle": "questionTitle"
            }
        """.trimIndent()

        val answer = Json.decodeFromString<Answer>(string = questionPostPayload)
        val databaseSaverMock = mockk<DatabaseSaver>()
        val answerPostHandler = AnswerPostHandler(databaseSaverMock)

        answerPostHandler.answer(questionPostPayload)
        verify(exactly = 1) { databaseSaverMock.save(answer) }
    }

})