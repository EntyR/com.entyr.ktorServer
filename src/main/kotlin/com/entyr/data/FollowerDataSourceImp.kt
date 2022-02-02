package com.entyr.data

import com.entyr.data.table.FollowerTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class FollowerDataSourceImp: FollowerDataSource {
    override suspend fun addFollower(author: String, followerEmail: String) {
        transaction {
            FollowerTable.insert { tb ->
                tb[follower] = followerEmail
                tb[FollowerTable.author] = author
            }
        }
    }

    override suspend fun deleteFollower(author: String, followerEmail: String) {
        transaction {
            FollowerTable.deleteWhere {
                FollowerTable.follower.eq(followerEmail) and FollowerTable.author.eq(author)
            }
        }

    }
}