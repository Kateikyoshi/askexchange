package models.answer

import kotlinx.datetime.Instant
import models.InnerId

data class InnerAnswer(
    var id: InnerId = InnerId.NONE,
    var body: String = "",
    var date: Instant = Instant.DISTANT_PAST,
    var likes: Long = 0
)