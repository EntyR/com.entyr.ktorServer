package com.entyr.data.table

import org.jetbrains.exposed.sql.Table

object FollowerTable: Table() {
    val author = varchar("auth",512).references(UsersTable.email)
    val follower = varchar("flw",512).references(UsersTable.email)
}