package ru.shirnin.askexchange.repo.postgre.tables

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

object QuestionTable : IdTable<String>("question") {
    override val id = varchar("id", 128).entityId()
    val title = varchar("title", 128)
    val body = largeText("body")
    val parentUserId = reference("parentUserId", UserTable.id)
    val lock = varchar("lock", 50)

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "PK_Question_Id")

    fun from(response: InsertStatement<Number>) = InnerQuestion(
        id = InnerId(response[id].toString()),
        title = response[title],
        body = response[body],
        parentUserId = InnerId(response[parentUserId].toString()),
        lock = InnerVersionLock(response[lock])
    )

    fun from(response: ResultRow) = InnerQuestion(
        id = InnerId(response[id].toString()),
        title = response[title],
        body = response[body],
        parentUserId = InnerId(response[parentUserId].toString()),
        lock = InnerVersionLock(response[lock])
    )

}