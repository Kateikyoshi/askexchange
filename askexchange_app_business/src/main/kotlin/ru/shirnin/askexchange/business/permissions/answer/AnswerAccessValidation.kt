package ru.shirnin.askexchange.business.permissions.answer

import ru.shirnin.askexchange.auth.checkPermitted
import ru.shirnin.askexchange.auth.resolveRelationsTo
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.chain
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerState
import ru.shirnin.askexchange.inner.models.helpers.fail

fun MultipleCommandBuilder<InnerAnswerContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Resolving access rights by principal(user) group and access permissions table"
    isContextHealthy { state == InnerState.RUNNING }
    worker("Resolving answer relation to principal(user)") {
        answerFetchedFromRepo.principalRelations = answerFetchedFromRepo resolveRelationsTo principal
    }
    worker("Resolving access to a answer") {
        permitted = checkPermitted(command, answerFetchedFromRepo.principalRelations, permissionsChain)
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

