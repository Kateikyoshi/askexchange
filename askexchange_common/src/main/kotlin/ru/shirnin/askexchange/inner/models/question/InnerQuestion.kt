package ru.shirnin.askexchange.inner.models.question

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.permissions.InnerPermissionClient
import ru.shirnin.askexchange.inner.permissions.InnerPrincipalRelations

data class InnerQuestion (
    var id: InnerId = InnerId.NONE,
    var title: String = "",
    var body: String = "",
    /**
     * Can't be mutated later (update is blocked)
     */
    var parentUserId: InnerId = InnerId.NONE,
    var principalRelations: Set<InnerPrincipalRelations> = emptySet(),
    val permissionsClient: MutableSet<InnerPermissionClient> = mutableSetOf(),
    var lock: InnerVersionLock = InnerVersionLock.NONE
) {
    fun deepCopy(): InnerQuestion = copy(
        principalRelations = principalRelations.toSet(),
        permissionsClient = permissionsClient.toMutableSet()
    )

    companion object {
        private val NONE = InnerQuestion()
    }
}