package ru.shirnin.askexchange.inner.models

data class InnerUser (
    var id: InnerId = InnerId.NONE,
    var login: String = "",
    var password: String = "",
    var email: String = "",
    var expertise: String = ""
)