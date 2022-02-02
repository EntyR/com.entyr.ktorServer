package com.entyr.data

import com.entyr.data.table.SocketChatTable
import com.entyr.data.table.UsersTable
import com.entyr.model.LastMessageModel
import com.entyr.model.Message
import com.entyr.model.UserInfo
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class MessageDataSourceImp: MessageDataSource {

    override suspend fun getMessagesFromUser(email: String, fromEmail: String, mark: Message?): List<Message> {
        return transaction {
            SocketChatTable.select {
                (SocketChatTable.sendToUser.eq(email) and SocketChatTable.authorUser.eq(fromEmail)) or
                        (SocketChatTable.authorUser.eq(email) and SocketChatTable.sendToUser.eq(fromEmail))
            }.sortedByDescending {
                SocketChatTable.timestamp
            }.map {
                Message(
                    userName = it[SocketChatTable.authorUser],
                    sendToUser = it[SocketChatTable.sendToUser],
                    text = it[SocketChatTable.text],
                    timestamp = it[SocketChatTable.timestamp],
                    url = it[SocketChatTable.uri],
                    xCord = it[SocketChatTable.xCord],
                    yCord = it[SocketChatTable.yCord],
                    reed = false
                )
            }
        }
    }

    override suspend fun getLastMessages(user: String): List<LastMessageModel> {
        val message =  transaction {
            val dialogs = SocketChatTable.select{
                SocketChatTable.sendToUser.eq(user) or SocketChatTable.authorUser.eq(user)
            }.groupBy {
                if (user == it[SocketChatTable.authorUser]) it[SocketChatTable.sendToUser]
                else it[SocketChatTable.authorUser]
            }.values.map {
                it.last()
            }.sortedByDescending {
                SocketChatTable.timestamp
            }

            dialogs.map {
                Message(
                    userName = it[SocketChatTable.authorUser],
                    sendToUser = it[SocketChatTable.sendToUser],
                    text = it[SocketChatTable.text],
                    url = it[SocketChatTable.uri],
                    timestamp = it[SocketChatTable.timestamp],
                    xCord = it[SocketChatTable.xCord],
                    yCord = it[SocketChatTable.yCord],
                    reed = false
                )
            }

        }
        return transaction {
            message.map {message->
                val user = UsersTable.select {
                    UsersTable.email.eq(message.userName)
                }.singleOrNull()?.let {
                    UserInfo(
                        it[UsersTable.email],
                        it[UsersTable.name],
                        it[UsersTable.profilePic],
                        it[UsersTable.desc]
                    )
                }
                val sendUser = UsersTable.select {
                    UsersTable.email.eq(message.sendToUser)
                }.singleOrNull()?.let {
                    UserInfo(
                        it[UsersTable.email],
                        it[UsersTable.name],
                        it[UsersTable.profilePic],
                        it[UsersTable.desc]
                    )
                }
                LastMessageModel(
                    message.userName,
                    message.sendToUser,
                    user!!.username,
                    sendUser!!.username,
                    user.profPic,
                    sendUser.profPic,
                    message.text,
                    message.timestamp,
                    1, 1,
                    message.url,
                    message.reed


                )
            }






        }
    }

    override suspend fun insertMessage(message: Message) {
        transaction {
            SocketChatTable.insert { tb ->
                tb[SocketChatTable.authorUser] = message.userName
                tb[sendToUser] = message.sendToUser
                tb[text] = message.text
                tb[SocketChatTable.uri] = message.url
                tb[timestamp] = message.timestamp
                tb[reed] = message.reed
                tb[xCord] = message.xCord
                tb[yCord] = message.yCord
            }
        }
    }
}