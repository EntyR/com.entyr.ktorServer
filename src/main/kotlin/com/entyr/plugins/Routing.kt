package com.entyr.plugins

import com.entyr.chat.ChatController
import com.entyr.data.MessageDataSource
import com.entyr.data.PhotoDataSource
import com.entyr.data.PostDataSource
import com.entyr.data.Users
import com.entyr.routes.*
import com.entyr.session.routeWithSession
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {


    routing {

        val postDataSource by inject<PostDataSource>()
        val chatController by inject<ChatController>()
        val photoDataSource by inject<PhotoDataSource>()
        val users by inject<Users>()
        val messageDataSource by inject<MessageDataSource>()
        routeWithSession {
            chatSocket(chatController)
        }
        postRoutes(postDataSource)
        photoRoutes(photoDataSource)
        userRoutes(users)
        getMessages(messageDataSource, users)
        getMessagesFromUser(messageDataSource)
        get("/") {
            call.respondText("Hello World!")
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }

    }
}
