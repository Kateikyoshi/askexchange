package ru.shirnin.askexchange.inner.models.question

import ru.shirnin.askexchange.inner.models.InnerId

data class InnerQuestion (
    var id: InnerId = InnerId.NONE,
    var title: String = "",
    var body: String = "",
    var username: String = ""
) {
    fun deepCopy(): InnerQuestion = copy()
}