package ru.shirnin.askexchange.auth

import ru.shirnin.askexchange.inner.permissions.InnerPermissionClient
import ru.shirnin.askexchange.inner.permissions.InnerPrincipalRelations
import ru.shirnin.askexchange.inner.permissions.InnerUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<InnerUserPermissions>,
    relations: Iterable<InnerPrincipalRelations>,
) = mutableSetOf<InnerPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                val principalRelationsToPermissionClientMap = accessTable[permission]
                val innerPermissionClient = principalRelationsToPermissionClientMap?.get(relation)
                if (innerPermissionClient != null) add(innerPermissionClient)
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    
    InnerUserPermissions.READ_OWN to mapOf(
        InnerPrincipalRelations.OWN to InnerPermissionClient.READ
    ),
    InnerUserPermissions.READ_GROUP to mapOf(
        InnerPrincipalRelations.GROUP to InnerPermissionClient.READ
    ),
    InnerUserPermissions.READ_PUBLIC to mapOf(
        InnerPrincipalRelations.PUBLIC to InnerPermissionClient.READ
    ),
    InnerUserPermissions.READ_CANDIDATE to mapOf(
        InnerPrincipalRelations.MODERATION_OK to InnerPermissionClient.READ
    ),

    // UPDATE
    InnerUserPermissions.UPDATE_OWN to mapOf(
        InnerPrincipalRelations.OWN to InnerPermissionClient.UPDATE
    ),
    InnerUserPermissions.UPDATE_PUBLIC to mapOf(
        InnerPrincipalRelations.MODERATION_OK to InnerPermissionClient.UPDATE
    ),
    InnerUserPermissions.UPDATE_CANDIDATE to mapOf(
        InnerPrincipalRelations.MODERATION_OK to InnerPermissionClient.UPDATE
    ),

    // DELETE
    InnerUserPermissions.DELETE_OWN to mapOf(
        InnerPrincipalRelations.OWN to InnerPermissionClient.DELETE
    ),
    InnerUserPermissions.DELETE_PUBLIC to mapOf(
        InnerPrincipalRelations.MODERATION_OK to InnerPermissionClient.DELETE
    ),
    InnerUserPermissions.DELETE_CANDIDATE to mapOf(
        InnerPrincipalRelations.MODERATION_OK to InnerPermissionClient.DELETE
    ),
)
