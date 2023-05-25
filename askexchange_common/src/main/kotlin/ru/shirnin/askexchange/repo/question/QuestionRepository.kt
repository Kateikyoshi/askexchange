package ru.shirnin.askexchange.repo.question

interface QuestionRepository {

    suspend fun createQuestion(request: DbQuestionRequest): DbQuestionResponse
    suspend fun readQuestion(request: DbQuestionIdRequest): DbQuestionResponse
    suspend fun updateQuestion(request: DbQuestionRequest): DbQuestionResponse
    suspend fun deleteQuestion(request: DbQuestionIdRequest): DbQuestionResponse

    companion object {
        val NONE = object : QuestionRepository {
            override suspend fun createQuestion(request: DbQuestionRequest): DbQuestionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateQuestion(request: DbQuestionRequest): DbQuestionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
                TODO("Not yet implemented")
            }

        }
    }
}