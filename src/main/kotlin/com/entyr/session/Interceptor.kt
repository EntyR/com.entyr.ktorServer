package com.entyr.session

import com.auth0.jwt.JWT
import com.entyr.model.Session
import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*

fun Route.routeWithSession(callback: Route.() -> Unit): Route {
    val routeWithSession = this.createChild(object : RouteSelector() {
        override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation =
            RouteSelectorEvaluation.Constant
    })

    routeWithSession.intercept(ApplicationCallPipeline.Features) {

        if (call.sessions.get<Session>() == null) {
            val username = call.request.headers["Authorization"]

            val decoded =
                JWT.decode(
                    Gson().toJson(username?.replace("Bearer ", ""))
                ).claims["username"].toString()
            val tkn = decoded.subSequence(1, decoded.length-1)
            println(tkn)
            call.sessions.set(Session(tkn.toString(), generateNonce()))
        } else call.sessions.set(Session("Unauthorized", generateNonce()))

    }


// Configure this route with the block provided by the user
callback(routeWithSession)

return routeWithSession
}