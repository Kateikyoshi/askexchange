package ru.shirnin.askexchange.business.repo.answer

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerWorkMode
import ru.shirnin.askexchange.inner.models.helpers.errorAdministration
import ru.shirnin.askexchange.inner.models.helpers.fail
import ru.shirnin.askexchange.repo.answer.AnswerRepository

fun MultipleCommandBuilder<InnerAnswerContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Determining a main repository state depending on the request 
    """.trimIndent()
    handle {
        answerRepo = when (workMode) {
            InnerWorkMode.TEST -> settings.answerRepoTest
            InnerWorkMode.STUB -> settings.answerRepoStub
            else -> settings.answerRepoProd
        }
        if (workMode != InnerWorkMode.STUB && answerRepo == AnswerRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is not configured for the chosen workMode ($workMode). "
            )
        )
    }
}