package ru.shirnin.askexchange.repo.postgre

import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.JdbcTestContainerExtension
import org.testcontainers.containers.PostgreSQLContainer
import ru.shirnin.askexchange.repo.tests.question.*

class QuestionRepoPostgreTests : FunSpec({

    val dbName = "askexchange"
    val user = "postgres"
    val password = "postgres"

    val postgresContainer = PostgreSQLContainer<Nothing>("postgres:15.2-alpine").apply {
        startupAttempts = 1
        withDatabaseName(dbName)
        withUsername(user)
        withPassword(password)
        withExposedPorts(5432)
    }

    install(JdbcTestContainerExtension(postgresContainer)) {
        poolName = "myconnectionpool"
        maximumPoolSize = 8
        idleTimeout = 10000
    }

    val exposedPort = postgresContainer.firstMappedPort

    val props = SqlProperties(
        url = "jdbc:postgresql://localhost:$exposedPort/$dbName",
        user = user,
        password = password,
        //dropDatabase = true
    )

    include(
        createSuccess(
            PostgreQuestionRepo(
                properties = props,
                initObjects = BaseCreate.initObjects
            )
        )
    )
    include(
        updateSuccess(
            PostgreQuestionRepo(
                properties = props,
                initObjects = BaseUpdate.initObjects
            )
        )
    )
    include(
        readSuccess(
            PostgreQuestionRepo(
                properties = props,
                initObjects = BaseRead.initObjects
            )
        )
    )
    include(
        deleteSuccess(
            PostgreQuestionRepo(
                properties = props,
                initObjects = BaseDelete.initObjects
            )
        )
    )
})