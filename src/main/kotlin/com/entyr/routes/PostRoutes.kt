package com.entyr.routes

import com.entyr.data.PostDataSource
import com.entyr.model.Post
import com.entyr.model.PostRequest
import com.entyr.model.Response
import com.entyr.model.User
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.lang.Exception

fun Route.postRoutes(
    postDb: PostDataSource
) {
    authenticate("auth-jwt") {
        post("/create-post"){

            val post = try {
                call.receive<PostRequest>()
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, Response(false,"Missing Fields"))
                return@post
            }

            try {

                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val postdb = Post(
                    null,
                    username,
                    post.url,
                    post.date,
                    post.text,
                    post.title,
                    post.where,
                    post.xCord,
                    post.yCord
                )
                postDb.createPost(postdb)
                call.respond(HttpStatusCode.OK, Response(true,"Note Added Successfully!"))

            } catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, Response(false,e.message ?: "Some Problem Occurred!"))
            }

        }
        get("/get-sub-posts"){
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            try {
                val notes = postDb.getSubscribedPosts(username)
                call.respond(HttpStatusCode.OK,notes)
            } catch (e: Exception){

                call.respond(HttpStatusCode.Conflict, emptyList<Post>())
            }
        }

        get("/get-posts"){

            try {
                val notes = postDb.getAllPosts()
                call.respond(HttpStatusCode.OK,notes)
            } catch (e: Exception){
                e.printStackTrace()
                call.respond(HttpStatusCode.Conflict, emptyList<Post>())
            }
        }
    }
}