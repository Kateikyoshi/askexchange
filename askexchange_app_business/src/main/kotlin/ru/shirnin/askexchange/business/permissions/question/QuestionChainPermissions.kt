package ru.shirnin.askexchange.business.permissions.question

import ru.shirnin.askexchange.auth.resolveChainPermissions
import ru.shirnin.askexchange.chain.dsl.MultipleCommandBuilder
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerState


fun MultipleCommandBuilder<InnerQuestionContext>.chainPermissions(title: String) = worker {
    this.title = title
    description = "Adding permissions for user groups"

    isContextHealthy { state == InnerState.RUNNING }

    handle {
        permissionsChain.addAll(resolveChainPermissions(principal.groups))
    }
}
