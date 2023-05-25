package ru.shirnin.askexchange.inner.models.answer

import kotlinx.datetime.Instant
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.inner.permissions.InnerPermissionClient
import ru.shirnin.askexchange.inner.permissions.InnerPrincipalRelations

data class InnerAnswer(
    var id: InnerId = InnerId.NONE,
    var body: String = "",
    var date: Instant = Instant.DISTANT_PAST,
    var likes: Long = 0,
    /**
     * Can't be mutated later (update is blocked)
     */
    var parentUserId: InnerId = InnerId.NONE,
    /**
     * Can't be mutated later (update is blocked)
     */
    var parentQuestionId: InnerId = InnerId.NONE,
    var principalRelations: Set<InnerPrincipalRelations> = emptySet(),
    val permissionsClient: MutableSet<InnerPermissionClient> = mutableSetOf(),
    var lock: InnerVersionLock = InnerVersionLock.NONE
) {
    fun deepCopy(): InnerAnswer = copy(
        principalRelations = principalRelations.toSet(),
        permissionsClient = permissionsClient.toMutableSet()
    )

    companion object {
        private val NONE = InnerQuestion()
    }
}