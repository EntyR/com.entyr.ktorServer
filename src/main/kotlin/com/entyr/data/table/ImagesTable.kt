package com.entyr.data.table

import org.jetbrains.exposed.sql.Table

object ImagesTable: Table() {

    val uri = text("uri")

    override val primaryKey: PrimaryKey = PrimaryKey(uri)
}