package ru.shirnin.askexchange.app.conf

import io.ktor.server.config.*

data class PostgresConfig(
    val url: String = "jdbc:postgresql://localhost:5432/askexchange_postgre_db",
    val user: String = "postgres",
    val password: String = "postgres",
    val schema: String = "postgres",
) {
    constructor(config: ApplicationConfig): this(
        url = config.property("$PATH.url").getString(),
        user = config.property("$PATH.user").getString(),
        password = config.property("$PATH.password").getString(),
        schema = config.property("$PATH.schema").getString(),
    )

    companion object {
        const val PATH = "askexchange.repository.psql"
    }
}