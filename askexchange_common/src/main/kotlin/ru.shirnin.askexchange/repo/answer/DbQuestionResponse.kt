package ru.shirnin.askexchange.repo.answer

import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.repo.DbResponse

data class DbAnswerResponse(
    override val data: InnerAnswer?,
    override val isSuccess: Boolean,
    override val errors: List<InnerError> = emptyList()
): DbResponse<InnerAnswer> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbAnswerResponse(null, true)
        fun success(result: InnerAnswer) = DbAnswerResponse(result, true)
        fun error(errors: List<InnerError>) = DbAnswerResponse(null, false, errors)
        fun error(error: InnerError) = DbAnswerResponse(null, false, listOf(error))
    }
}