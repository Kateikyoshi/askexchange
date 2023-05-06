package ru.shirnin.askexchange.repo.postgre

class SqlProperties (
    val url: String = "jdbc:postgresql://localhost:5432/askexchange_postgre_db",//jdbc:postgresql://psql:5432/askexchange
    val user: String = "postgres",
    val password: String = "postgres",
    val schema: String = "askexchange",
    val dropDatabase: Boolean = false,
    val fastMigration: Boolean = true
)