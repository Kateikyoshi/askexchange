package ru.shirnin.askexchange.repo.stubs

import InnerQuestionStub
import ru.shirnin.askexchange.repo.question.DbQuestionIdRequest
import ru.shirnin.askexchange.repo.question.DbQuestionRequest
import ru.shirnin.askexchange.repo.question.DbQuestionResponse
import ru.shirnin.askexchange.repo.question.QuestionRepository

class QuestionRepositoryStub: QuestionRepository {
    override suspend fun createQuestion(request: DbQuestionRequest): DbQuestionResponse {
        return DbQuestionResponse(
            data = InnerQuestionStub.prepareResult {  },
            isSuccess = true
        )
    }

    override suspend fun readQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
        return DbQuestionResponse(
            data = InnerQuestionStub.prepareResult {  },
            isSuccess = true
        )
    }

    override suspend fun updateQuestion(request: DbQuestionRequest): DbQuestionResponse {
        return DbQuestionResponse(
            data = InnerQuestionStub.prepareResult {  },
            isSuccess = true
        )
    }

    override suspend fun deleteQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
        return DbQuestionResponse(
            data = InnerQuestionStub.prepareResult {  },
            isSuccess = true
        )
    }

}