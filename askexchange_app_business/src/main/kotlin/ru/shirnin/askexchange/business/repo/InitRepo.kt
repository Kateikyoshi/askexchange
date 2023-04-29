package ru.shirnin.askexchange.business.repo

import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerWorkMode
import ru.shirnin.askexchange.inner.models.helpers.errorAdministration
import ru.shirnin.askexchange.inner.models.helpers.fail
import ru.shirnin.askexchange.repo.question.QuestionRepository

fun MultipleCommandBuilder<InnerQuestionContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Determining a main repository state depending on the request 
    """.trimIndent()
    handle {
        questionRepo = when (workMode) {
            InnerWorkMode.TEST -> settings.repoTest
            InnerWorkMode.STUB -> settings.repoStub
            else -> settings.repoProd
        }
        if (workMode != InnerWorkMode.STUB && questionRepo == QuestionRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is not configured for the chosen workMode ($workMode). "
            )
        )
    }
}