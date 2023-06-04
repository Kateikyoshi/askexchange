package ru.shirnin.askexchange.repo.question

import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.DbResponse

data class DbQuestionResponse(
    override val data: InnerQuestion?,
    override val isSuccess: Boolean,
    override val errors: List<InnerError> = emptyList()
): DbResponse<InnerQuestion> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbQuestionResponse(null, true)
        fun success(result: InnerQuestion) = DbQuestionResponse(result, true)
        fun error(errors: List<InnerError>) = DbQuestionResponse(null, false, errors)
        fun error(error: InnerError) = DbQuestionResponse(null, false, listOf(error))
    }
}