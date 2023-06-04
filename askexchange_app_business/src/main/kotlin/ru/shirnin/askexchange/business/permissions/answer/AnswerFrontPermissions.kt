package ru.shirnin.askexchange.business.permissions.answer

import ru.shirnin.askexchange.auth.resolveFrontPermissions
import ru.shirnin.askexchange.auth.resolveRelationsTo
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerState


fun MultipleCommandBuilder<InnerAnswerContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Resolving permissions for the frontend"

    isContextHealthy { state == InnerState.RUNNING }

    handle {
        answerRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                answerRepoDone resolveRelationsTo principal
            )
        )

        for (answer in answersRepoDone) {
            answer.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    answer.resolveRelationsTo(principal)
                )
            )
        }
    }
}
