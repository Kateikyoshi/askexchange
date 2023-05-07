package ru.shirnin.askexchange.inner.models.question

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock

data class InnerQuestion (
    var id: InnerId = InnerId.NONE,
    var title: String = "",
    var body: String = "",
    /**
     * Can't be mutated later (update is blocked)
     */
    var parentUserId: InnerId = InnerId.NONE,
    var lock: InnerVersionLock = InnerVersionLock.NONE
) {
    fun deepCopy(): InnerQuestion = copy()

    companion object {
        private val NONE = InnerQuestion()
    }
}