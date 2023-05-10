package ru.shirnin.askexchange.repo.stubs

import ru.shirnin.askexchange.repo.answer.AnswerRepository
import ru.shirnin.askexchange.repo.answer.DbAnswerIdRequest
import ru.shirnin.askexchange.repo.answer.DbAnswerRequest
import ru.shirnin.askexchange.repo.answer.DbAnswerResponse

class AnswerRepositoryStub: AnswerRepository {
    override suspend fun createAnswer(request: DbAnswerRequest): DbAnswerResponse {
        return DbAnswerResponse(
            data = InnerAnswerStub.prepareResult {  },
            isSuccess = true
        )
    }

    override suspend fun readAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
        return DbAnswerResponse(
            data = InnerAnswerStub.prepareResult {  },
            isSuccess = true
        )
    }

    override suspend fun updateAnswer(request: DbAnswerRequest): DbAnswerResponse {
        return DbAnswerResponse(
            data = InnerAnswerStub.prepareResult {  },
            isSuccess = true
        )
    }

    override suspend fun deleteAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
        return DbAnswerResponse(
            data = InnerAnswerStub.prepareResult {  },
            isSuccess = true
        )
    }

}