package com.entyr.data.table

import org.jetbrains.exposed.sql.Table

object UsersTable: Table() {
    val email = varchar("email",512)
    val name = varchar("name",512)
    val hashPassword = varchar("hashPassword",512)
    val profilePic = text("url").default("")
    val desc = text("desc").default("")
    override val primaryKey: Table.PrimaryKey = PrimaryKey(email)


}