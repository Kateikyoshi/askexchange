package ru.shirnin.askexchange.app.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import io.ktor.server.application.*
import ru.shirnin.askexchange.app.AskAppSettings
import ru.shirnin.askexchange.app.conf.KtorAuthConfig
import ru.shirnin.askexchange.app.conf.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.shirnin.askexchange.app.conf.resolveAlgorithm

fun Application.configureSecurity(appSettings: AskAppSettings, authSettings: KtorAuthConfig) {
    val loggerSecurity = appSettings.chainSettings.loggerProvider.logger(Application::configureSecurity::class)

    authentication {
            jwt("auth-jwt") {
                realm = authSettings.realm

                verifier { httpAuthHeader ->
                    val algorithm = httpAuthHeader.resolveAlgorithm(authSettings)
                    JWT
                        .require(algorithm)
                        .withAudience(authSettings.audience)
                        .withIssuer(authSettings.issuer)
                        .build()
                }
                validate { credential ->
                    when {
                        credential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                            loggerSecurity.error("Groups claim must not be empty in JWT token")
                            null
                        }
                        else -> JWTPrincipal(credential.payload)
                    }
                }
            }
        }
}
