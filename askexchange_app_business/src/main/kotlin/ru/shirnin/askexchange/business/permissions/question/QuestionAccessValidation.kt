package ru.shirnin.askexchange.business.permissions.question

import ru.shirnin.askexchange.auth.checkPermitted
import ru.shirnin.askexchange.auth.resolveRelationsTo
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.chain
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.inner.models.helpers.fail

fun MultipleCommandBuilder<InnerQuestionContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Resolving access rights by principal(user) group and access permissions table"
    isContextHealthy { state == InnerState.RUNNING }
    worker("Resolving question relation to principal(user)") {
        questionFetchedFromRepo.principalRelations = questionFetchedFromRepo resolveRelationsTo principal
    }
    worker("Resolving access to a question") {
        permitted = checkPermitted(command, questionFetchedFromRepo.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Access permission validation"
        description = "Resolving permissions for an operation"
        isContextHealthy { !permitted }
        handle {
            fail(InnerError(message = "User is not allowed to perform this operation"))
        }
    }
}

