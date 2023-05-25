package ru.shirnin.askexchange.auth

import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.inner.permissions.InnerPrincipalModel
import ru.shirnin.askexchange.inner.permissions.InnerPrincipalRelations

infix fun InnerQuestion.resolveRelationsTo(principal: InnerPrincipalModel): Set<InnerPrincipalRelations> = setOfNotNull(
    InnerPrincipalRelations.NONE,
    InnerPrincipalRelations.NEW.takeIf { this.id == InnerId.NONE },
    InnerPrincipalRelations.OWN.takeIf { this.parentUserId == principal.id },
    //InnerPrincipalRelations.MODERATION_OK.takeIf { visibility != InnerVisibility.VISIBLE_TO_OWNER },
    //InnerPrincipalRelations.PUBLIC.takeIf { visibility == InnerVisibility.VISIBLE_PUBLIC },
)

infix fun InnerAnswer.resolveRelationsTo(principal: InnerPrincipalModel): Set<InnerPrincipalRelations> = setOfNotNull(
    InnerPrincipalRelations.NONE,
    InnerPrincipalRelations.NEW.takeIf { this.id == InnerId.NONE },
    InnerPrincipalRelations.OWN.takeIf { this.parentUserId == principal.id },
    //InnerPrincipalRelations.MODERATION_OK.takeIf { visibility != InnerVisibility.VISIBLE_TO_OWNER },
    //InnerPrincipalRelations.PUBLIC.takeIf { visibility == InnerVisibility.VISIBLE_PUBLIC },
)
