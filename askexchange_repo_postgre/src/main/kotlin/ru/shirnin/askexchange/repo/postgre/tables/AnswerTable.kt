package ru.shirnin.askexchange.repo.postgre.tables

import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer

object AnswerTable : Table("answer") {
    val id = varchar("id", 128).index()
    val body = largeText("body")
    val date = varchar("date", 128)
    val likes = integer("likes")
    val lock = varchar("lock", 50)

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "PK_Answer_Id")

    fun from(response: InsertStatement<Number>) = InnerAnswer(
        id = InnerId(response[id].toString()),
        body = response[body],
        date = Instant.parse(response[date]),
        likes = response[likes].toLong(),
        lock = InnerVersionLock(response[lock])
    )
}