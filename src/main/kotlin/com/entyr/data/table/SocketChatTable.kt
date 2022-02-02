package com.entyr.data.table

import org.jetbrains.exposed.sql.Table

object SocketChatTable: Table() {
    val id  = integer("id")
        .autoIncrement("some sequence")
    val text = text("text")
    val timestamp = long("timestamp")
    val authorUser = varchar("aut-email",512).references(UsersTable.email)
    val sendToUser = varchar("send-email",512).references(UsersTable.email)
    val uri = text("image_url")
    val reed = bool("reed")
    val xCord = long("xCord")
    val yCord = long("yCord")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}