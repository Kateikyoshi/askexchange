package ru.shirnin.askexchange.app

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.request.*
import ru.shirnin.askexchange.app.conf.KtorAuthConfig

fun HttpRequestBuilder.addAuth(
    id: String = "user1",
    groups: List<String> = listOf("USER", "TEST"),
    config: KtorAuthConfig = KtorAuthConfig.TEST,
) {
    val token = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim(KtorAuthConfig.GROUPS_CLAIM, groups)
        .withClaim(KtorAuthConfig.ID_CLAIM, id)
        .sign(Algorithm.HMAC256(config.secret))

    bearerAuth(token)
}