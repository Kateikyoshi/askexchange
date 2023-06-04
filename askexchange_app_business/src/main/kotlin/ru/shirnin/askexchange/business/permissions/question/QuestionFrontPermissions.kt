package ru.shirnin.askexchange.business.permissions.question

import ru.shirnin.askexchange.auth.resolveFrontPermissions
import ru.shirnin.askexchange.auth.resolveRelationsTo
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState


fun MultipleCommandBuilder<InnerQuestionContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Resolving permissions for the frontend"

    isContextHealthy { state == InnerState.RUNNING }

    handle {
        questionRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                questionRepoDone resolveRelationsTo principal
            )
        )

        for (question in questionsRepoDone) {
            question.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    question.resolveRelationsTo(principal)
                )
            )
        }
    }
}
