package ru.shirnin.askexchange.repo.postgre.tables

import org.jetbrains.exposed.dao.id.IdTable

object UserTable : IdTable<String>("user") {
    override val id = varchar("id", 50).entityId()
    val name = varchar("name", 128)

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "PK_User_Id")
}
