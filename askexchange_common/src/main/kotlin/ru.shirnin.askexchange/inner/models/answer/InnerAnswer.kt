package ru.shirnin.askexchange.inner.models.answer

import kotlinx.datetime.Instant
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

data class InnerAnswer(
    var id: InnerId = InnerId.NONE,
    var body: String = "",
    var date: Instant = Instant.DISTANT_PAST,
    var likes: Long = 0
) {
    fun deepCopy(): InnerAnswer = copy()
}