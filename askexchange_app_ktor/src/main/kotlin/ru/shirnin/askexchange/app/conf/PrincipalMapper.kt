package ru.shirnin.askexchange.app.conf

import io.ktor.server.auth.jwt.*
import ru.shirnin.askexchange.app.conf.KtorAuthConfig.Companion.F_NAME_CLAIM
import ru.shirnin.askexchange.app.conf.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.shirnin.askexchange.app.conf.KtorAuthConfig.Companion.ID_CLAIM
import ru.shirnin.askexchange.app.conf.KtorAuthConfig.Companion.L_NAME_CLAIM
import ru.shirnin.askexchange.app.conf.KtorAuthConfig.Companion.M_NAME_CLAIM
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.permissions.InnerPrincipalModel
import ru.shirnin.askexchange.inner.permissions.InnerUserGroups

fun JWTPrincipal?.toModel() = this?.run {
    InnerPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { InnerId(it) } ?: InnerId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> InnerUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: InnerPrincipalModel.NONE
