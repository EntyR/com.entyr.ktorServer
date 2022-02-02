package com.entyr.chat

import com.entyr.data.MessageDataSource
import com.entyr.model.Message
import com.entyr.model.SessionMember
import io.ktor.http.cio.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, SessionMember>()

    fun onJoin(
        userEmail: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(userEmail)) {
            throw MemberAlreadyExistException()
        }
        members[userEmail] = SessionMember(
            userEmail = userEmail,
            sessionId = sessionId,
            socket = socket
        )
    }

    suspend fun sendMessage(message: Message) {

        val destination = members.values.filter {
            (it.userEmail == message.sendToUser) ||
                    (it.userEmail == message.userName)
        }
        members.values.forEach { println("members + $it") }
        println(message)
        messageDataSource.insertMessage(message)

        println("member $destination")

        destination.forEach {
            val parsedMessage = Json.encodeToString(message)
            it.socket.send(Frame.Text(parsedMessage))
        }


    }


    suspend fun tryDisconnect(username: String) {
        members[username]?.socket?.close()
        if (members.containsKey(username)) {
            members.remove(username)
        }
    }
}