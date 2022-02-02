package com.entyr.routes

import com.entyr.chat.ChatController
import com.entyr.chat.MemberAlreadyExistException
import com.entyr.data.MessageDataSource
import com.entyr.data.Users
import com.entyr.model.AddMessageRequest
import com.entyr.model.Response
import com.entyr.model.Session
import com.entyr.model.UserInfo
import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.chatSocket(
    roomController: ChatController,

    ) {


    authenticate("auth-jwt") {
        webSocket("/chat-socket") {



            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val session = call.sessions.get<Session>()
            println(this)
            if (session == null) {

                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
                return@webSocket
            }
            try {

                roomController.onJoin(
                    userEmail = session.userEmail,
                    sessionId = session.sessionId,
                    socket = this
                )
                incoming.consumeEach { frame ->
                    println("frame is $frame")
                    if (frame is Frame.Text) {
                        val messageRequest = Json
                            .decodeFromString<AddMessageRequest>(frame.readText())
                        roomController.sendMessage(
                            messageRequest.toMessage(username)
                        )
                    }
                }
            } catch (e: MemberAlreadyExistException) {
                e.printStackTrace()
//                call.respond(HttpStatusCode.Conflict)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                println("finall")
                roomController.tryDisconnect(session.userEmail)
            }
        }




    }
}

fun Route.getMessages(messageDataSource: MessageDataSource, userDataSource: Users) {
    authenticate("auth-jwt") {

        get("/user-messages-list") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val messages = messageDataSource.getLastMessages(username)
            call.respond(
                HttpStatusCode.OK,
                messages
            )
        }
        get("/user-list") {
            val users = userDataSource.getUsers(0)
            call.respond(
                HttpStatusCode.OK,
                users
            )
        }
        get("/userbyname") {
            val name = call.receiveText()
            val users = userDataSource.getUsersByName(name,0)
            call.respond(
                HttpStatusCode.OK,
                users
            )
        }
        get("/userinfo") {
            val userEmail = call.receiveText()
            val userInfo = userDataSource.getUserInfo(userEmail)
            if (userInfo == null) call.respond(HttpStatusCode.BadRequest, "Wrong Email")
            else call.respond(
                HttpStatusCode.OK,
                userInfo
            )
        }
        post("/update-user") {
            val updateRequest = try {
                call.receive<UserInfo>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, Response(false, "Missing Some Fields"))
                return@post
            }
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()

            if (username!= updateRequest.email){
                call.respond(HttpStatusCode.BadRequest, Response(false, "Unauthorised"))
            }
            userDataSource.updateUserInfo(updateRequest)
            call.respond(HttpStatusCode.OK)

        }
    }
}

fun Route.getMessagesFromUser(messageDataSource: MessageDataSource) {
    authenticate("auth-jwt") {

        get("/user-messages-fromuser") {
            val sendToEmail = call.receiveText()
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val messages = messageDataSource.getMessagesFromUser(username, sendToEmail)
            call.respond(
                HttpStatusCode.OK,
                messages
            )
        }


    }

}