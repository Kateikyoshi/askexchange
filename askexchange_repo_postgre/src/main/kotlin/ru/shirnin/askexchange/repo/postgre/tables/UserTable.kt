package ru.shirnin.askexchange.repo.postgre.tables

import org.jetbrains.exposed.dao.id.IdTable

/**
 * This table is fictional. There are no CRUD endpoints for this entity.
 * Therefore, ID will be filled randomly when Question or Answer are CREATED and etc.
 */
object UserTable : IdTable<String>("user") {
    /**
     * Can't be filled directly. Only gets filled as a side effect of a manipulation with Question or Answer
     */
    override val id = varchar("id", 50).entityId()
    val login = varchar("name", 50).nullable()
    val password = varchar("password", 50).nullable()
    val email = varchar("email", 50).nullable()
    val expertise = varchar("expertise", 50).nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "PK_User_Id")
}
