package ru.shirnin.askexchange.repo.tests

import ru.shirnin.askexchange.repo.question.DbQuestionIdRequest
import ru.shirnin.askexchange.repo.question.DbQuestionRequest
import ru.shirnin.askexchange.repo.question.DbQuestionResponse
import ru.shirnin.askexchange.repo.question.QuestionRepository

class QuestionRepositoryMock(
    private val invokeCreateQuestion: (DbQuestionRequest) -> DbQuestionResponse = { DbQuestionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadQuestion: (DbQuestionIdRequest) -> DbQuestionResponse = { DbQuestionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateQuestion: (DbQuestionRequest) -> DbQuestionResponse = { DbQuestionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteQuestion: (DbQuestionIdRequest) -> DbQuestionResponse = { DbQuestionResponse.MOCK_SUCCESS_EMPTY },
): QuestionRepository {
    override suspend fun createQuestion(request: DbQuestionRequest): DbQuestionResponse {
        return invokeCreateQuestion(request)
    }

    override suspend fun readQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
        return invokeReadQuestion(request)
    }

    override suspend fun updateQuestion(request: DbQuestionRequest): DbQuestionResponse {
        return invokeUpdateQuestion(request)
    }

    override suspend fun deleteQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
        return invokeDeleteQuestion(request)
    }
}