package com.entyr.plugins

import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.util.*
import io.ktor.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.entyr.model.Session
import com.google.gson.Gson
import com.google.gson.annotations.JsonAdapter
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.configureSession() {
    install(Sessions) {
        cookie<Session>("SESSION")
    }


}
