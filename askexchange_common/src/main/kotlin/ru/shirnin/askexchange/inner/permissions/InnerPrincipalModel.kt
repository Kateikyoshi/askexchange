package ru.shirnin.askexchange.inner.permissions

import ru.shirnin.askexchange.inner.models.InnerId

data class InnerPrincipalModel(
    val id: InnerId = InnerId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<InnerUserGroups> = emptySet()
) {
    companion object {
        val NONE = InnerPrincipalModel()
    }
}
