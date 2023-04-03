package ru.shirnin.askexchange.app.plugins

import io.ktor.server.plugins.cachingheaders.*
import io.ktor.http.content.*
import io.ktor.http.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.application.*
import ru.shirnin.askexchange.app.AskAppSettings

fun Application.configureHTTP(appSettings: AskAppSettings) {
    install(CachingHeaders) {
        options { call, outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
                else -> null
            }
        }
    }
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    install(CORS) {
        allowMethod(HttpMethod.Post)
        allowSameOrigin = true
        allowNonSimpleContentTypes = true
        appSettings.appUrls.forEach {

            val split = it.split("://")

            when (split.size) {
                2 -> allowHost(split[1].split("/")[0], listOf(split[0]))

                1 -> allowHost(split[0].split("/")[0], listOf("http", "https"))
            }
        }
    }
}
