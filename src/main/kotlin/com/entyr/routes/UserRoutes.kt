package com.entyr.routes

import com.entyr.data.Users
import com.entyr.model.LoginRequest
import com.entyr.model.RegisterRequest
import com.entyr.model.Response
import com.entyr.model.UserInfo
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.userRoutes(
    db: Users,
) {




    post("/users/register") {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadGateway, Response(false, "Missing some fields"))
            return@post
        }

        try {
            val token = db.addUser(registerRequest)
            call.respond(HttpStatusCode.OK, Response(true, token))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, Response(false, e.message ?: "Some Problem Occurred!"))
        }
    }
    post("/users/login") {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, Response(false, "Missing Some Fields"))
            return@post
        }

        try {
            val token = db.loginUser(loginRequest)

            if (token == null) {
                call.respond(HttpStatusCode.BadRequest, Response(false, "Incorrect email or password"))
            } else {
                call.respond(HttpStatusCode.OK, Response(true, token))
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, Response(false, "Missing Some Fields"))
        }
    }





}

