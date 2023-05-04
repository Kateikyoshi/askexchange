package ru.shirnin.askexchange.inner.models.question

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock

data class InnerQuestion (
    var id: InnerId = InnerId.NONE,
    var title: String = "",
    var body: String = "",
    var username: String = "",
    var lock: InnerVersionLock = InnerVersionLock.NONE
) {
    fun deepCopy(): InnerQuestion = copy()

    companion object {
        private val NONE = InnerQuestion()
    }
}