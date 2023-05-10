package ru.shirnin.askexchange.repo.answer

interface AnswerRepository {

    suspend fun createAnswer(request: DbAnswerRequest): DbAnswerResponse
    suspend fun readAnswer(request: DbAnswerIdRequest): DbAnswerResponse
    suspend fun updateAnswer(request: DbAnswerRequest): DbAnswerResponse
    suspend fun deleteAnswer(request: DbAnswerIdRequest): DbAnswerResponse

    companion object {
        val NONE = object : AnswerRepository {
            override suspend fun createAnswer(request: DbAnswerRequest): DbAnswerResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateAnswer(request: DbAnswerRequest): DbAnswerResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
                TODO("Not yet implemented")
            }

        }
    }
}