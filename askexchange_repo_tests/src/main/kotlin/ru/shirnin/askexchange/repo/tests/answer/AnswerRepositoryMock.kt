package ru.shirnin.askexchange.repo.tests.answer

import ru.shirnin.askexchange.repo.answer.DbAnswerIdRequest
import ru.shirnin.askexchange.repo.answer.DbAnswerRequest
import ru.shirnin.askexchange.repo.answer.DbAnswerResponse
import ru.shirnin.askexchange.repo.answer.AnswerRepository

class AnswerRepositoryMock(
    private val invokeCreateAnswer: (DbAnswerRequest) -> DbAnswerResponse = { DbAnswerResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadAnswer: (DbAnswerIdRequest) -> DbAnswerResponse = { DbAnswerResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateAnswer: (DbAnswerRequest) -> DbAnswerResponse = { DbAnswerResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteAnswer: (DbAnswerIdRequest) -> DbAnswerResponse = { DbAnswerResponse.MOCK_SUCCESS_EMPTY },
): AnswerRepository {
    override suspend fun createAnswer(request: DbAnswerRequest): DbAnswerResponse {
        return invokeCreateAnswer(request)
    }

    override suspend fun readAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
        return invokeReadAnswer(request)
    }

    override suspend fun updateAnswer(request: DbAnswerRequest): DbAnswerResponse {
        return invokeUpdateAnswer(request)
    }

    override suspend fun deleteAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
        return invokeDeleteAnswer(request)
    }
}