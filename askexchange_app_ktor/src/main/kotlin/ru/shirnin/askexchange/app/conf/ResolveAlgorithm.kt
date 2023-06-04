package ru.shirnin.askexchange.app.conf

import com.auth0.jwk.Jwk
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.auth.*
import java.net.URL
import java.security.interfaces.RSAPublicKey

fun HttpAuthHeader.resolveAlgorithm(authSettings: KtorAuthConfig): Algorithm = when {
    authSettings.certUrl != null -> resolveAlgorithmKeycloak(authSettings)
    else -> Algorithm.HMAC256(authSettings.secret)
}

private val jwks = mutableMapOf<String, Jwk>()

fun HttpAuthHeader.resolveAlgorithmKeycloak(authSettings: KtorAuthConfig): Algorithm {
    val tokenString = this.render().replace(this.authScheme, "").trim()

    if (tokenString.isBlank()) {
        throw IllegalArgumentException("Request contains no proper Authorization header")
    }

    val token = try {
        JWT.decode(tokenString)
    } catch (e: Exception) {
        throw IllegalArgumentException("Cannot parse JWT token from request", e)
    }

    if (token.algorithm != "RS256") {
        throw IllegalArgumentException("Wrong algorithm in JWT (${token.algorithm}). Must be ...")
    }

    val jwk = jwks[token.keyId] ?: run {
        val provider = UrlJwkProvider(URL(authSettings.certUrl))
        val jwk = provider.get(token.keyId)
        jwks[token.keyId] = jwk
        jwk
    }

    val publicKey = jwk.publicKey
    if (publicKey !is RSAPublicKey) {
        throw IllegalArgumentException("Key with ID was found in JWKS but is not a RSA-key.")
    }

    return Algorithm.RSA256(publicKey, null)
}
