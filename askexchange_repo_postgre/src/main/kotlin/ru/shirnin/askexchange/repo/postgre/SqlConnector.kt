package ru.shirnin.askexchange.repo.postgre

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

class SqlConnector(
    private val properties: SqlProperties,
    private val databaseConfig: DatabaseConfig = DatabaseConfig { }
) {
    // Sample of describing different db drivers in case of multiple DB connections with different data bases
    private enum class DbType(val driver: String) {
        MYSQL("com.mysql.cj.jdbc.Driver"),
        POSTGRESQL("org.postgresql.Driver")
    }

    // Sample of different db types
    private val dbType: DbType = properties.url.let { url ->
        when {
            url.startsWith("jdbc:mysql://") -> DbType.MYSQL
            url.startsWith("jdbc:postgresql://") -> DbType.POSTGRESQL
            else -> error("Cannot parse database type from url: $url. `jdbc:mysql://...` and `jdbc:postgresql://` are supported only.")
        }
    }

    // Global connection to PSQL
    private val globalConnection = Database.connect(
        properties.url, dbType.driver,
        properties.user, properties.password, databaseConfig = databaseConfig
    )

    // Ensure creation of new connection with options to migrate/pre-drop database
    fun connect(vararg tables: Table): Database {
        // Create schema if such not exists
        transaction(globalConnection) {
            when (dbType) {
                DbType.MYSQL -> {
                    val dbSettings = " DEFAULT CHARACTER SET utf8mb4\n" +
                            " DEFAULT COLLATE utf8mb4_general_ci"
                    connection.prepareStatement(
                        "CREATE DATABASE IF NOT EXISTS ${properties.schema}\n$dbSettings",
                        false
                    )
                        .executeUpdate()
                    connection.prepareStatement("ALTER DATABASE ${properties.schema}\n$dbSettings", false)
                        .executeUpdate()
                }

                DbType.POSTGRESQL -> {
                    connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS ${properties.schema}", false)
                        .executeUpdate()
                }
            }
        }

        // Create connection for all supported db types
        val connect = Database.connect(
            properties.url, dbType.driver, properties.user, properties.password,
            databaseConfig = databaseConfig,
            setupConnection = { connection ->
                when (dbType) {
                    DbType.MYSQL -> {
                        connection.schema = properties.schema
                        connection.catalog = properties.schema
                    }

                    DbType.POSTGRESQL -> {
                        connection.schema = properties.schema
                    }
                }
            }
        )

        // Update schema:
        //   - drop if needed (for ex, in tests);
        //   - exec migrations if needed;
        //   - otherwise unsure to create tables
        transaction(connect) {
            if (properties.dropDatabase) {
                SchemaUtils.drop(*tables, inBatch = true)
                SchemaUtils.create(*tables, inBatch = true)
            } else if (!properties.fastMigration) {
                // TODO: Place to exec migration: create and ensure tables
            } else {
                SchemaUtils.createMissingTablesAndColumns(*tables, inBatch = true)
            }
        }

        return connect
    }
}
