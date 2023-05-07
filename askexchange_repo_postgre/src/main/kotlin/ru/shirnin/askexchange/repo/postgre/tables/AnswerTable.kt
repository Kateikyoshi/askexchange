package ru.shirnin.askexchange.repo.postgre.tables

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.repo.postgre.tables.QuestionTable.entityId

object AnswerTable : IdTable<String>("answer") {
    override val id = varchar("id", 128).entityId()
    val body = largeText("body")
    val date = varchar("date", 128)
    val likes = integer("likes")
    val parentUserId = reference("parentUserId", UserTable.id)
    val parentQuestionId = reference("parentQuestionId", QuestionTable.id)
    val lock = varchar("lock", 50)

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "PK_Answer_Id")

    fun from(response: InsertStatement<Number>) = InnerAnswer(
        id = InnerId(response[id].toString()),
        body = response[body],
        date = Instant.parse(response[date]),
        likes = response[likes].toLong(),
        parentUserId = InnerId(response[parentUserId].toString()),
        parentQuestionId = InnerId(response[parentQuestionId].toString()),
        lock = InnerVersionLock(response[lock])
    )
}