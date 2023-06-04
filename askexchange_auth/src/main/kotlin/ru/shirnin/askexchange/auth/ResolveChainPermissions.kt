package ru.shirnin.askexchange.auth

import ru.shirnin.askexchange.inner.permissions.InnerUserGroups
import ru.shirnin.askexchange.inner.permissions.InnerUserPermissions

fun resolveChainPermissions(
    groups: Iterable<InnerUserGroups>,
) = 
    mutableSetOf<InnerUserPermissions>()
    .apply {
        addAll(groups.flatMap { greenlitGroupPermissions[it] ?: emptySet() })
        removeAll(groups.flatMap { revokedGroupPermissions[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val greenlitGroupPermissions = mapOf(
    InnerUserGroups.USER to setOf(
        InnerUserPermissions.READ_OWN,
        InnerUserPermissions.READ_PUBLIC,
        InnerUserPermissions.CREATE_OWN,
        InnerUserPermissions.UPDATE_OWN,
        InnerUserPermissions.DELETE_OWN
    ),
    InnerUserGroups.MODERATOR to setOf(),
    InnerUserGroups.ADMIN to setOf(),
    InnerUserGroups.TEST to setOf(),
    InnerUserGroups.BAN to setOf(),
)

private val revokedGroupPermissions = mapOf(
    InnerUserGroups.USER to setOf(),
    InnerUserGroups.MODERATOR to setOf(),
    InnerUserGroups.ADMIN to setOf(),
    InnerUserGroups.TEST to setOf(),
    InnerUserGroups.BAN to setOf(
        InnerUserPermissions.UPDATE_OWN,
        InnerUserPermissions.CREATE_OWN,
    ),
)
