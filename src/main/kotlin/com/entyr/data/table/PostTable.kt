package com.entyr.data.table

import org.jetbrains.exposed.sql.Table

object PostTable: Table() {
    val id = integer("id").
            autoIncrement("mydatabase")
    val uri = text("uri").references(ImagesTable.uri)
    val author = text("auth")
    val date = long("date")
    val text = text("text")
    val title = text("title")
    val where = text("where")
    val xCord = long("x-c")
    val yCord = long("y-c")
    override val primaryKey: PrimaryKey = PrimaryKey(id)


}