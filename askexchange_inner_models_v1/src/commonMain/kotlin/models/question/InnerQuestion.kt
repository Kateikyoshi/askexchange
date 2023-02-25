package models.question

import models.InnerId

data class InnerQuestion (
    var id: InnerId = InnerId.NONE,
    var title: String = "",
    var body: String = "",
    var username: String = ""
)