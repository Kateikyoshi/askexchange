package ru.shirnin.askexchange.repo.postgre.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

object QuestionTable : Table("question") {
    val id = varchar("id", 128).index()
    val title = varchar("title", 128)
    val body = largeText("body")
    val user = reference("userId", UserTable.id)
    val lock = varchar("lock", 50)

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "PK_Question_Id")

    fun from(response: InsertStatement<Number>) = InnerQuestion(
        id = InnerId(response[id].toString()),
        title = response[title],
        body = response[body],
        username = response[user].toString(),
        lock = InnerVersionLock(response[lock])
    )
}