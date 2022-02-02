package com.entyr.data

import com.entyr.model.LastMessageModel
import com.entyr.model.Message

interface MessageDataSource {

    suspend fun getMessagesFromUser(email: String, fromEmail: String, mark: Message? = null): List<Message>

    suspend fun getLastMessages(user: String): List<LastMessageModel>

    suspend fun insertMessage(message: Message)
}