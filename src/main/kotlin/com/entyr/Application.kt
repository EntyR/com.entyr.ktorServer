package com.entyr

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.entyr.di.mainModule
import com.entyr.plugins.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin


fun main() {

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(Koin) {
            modules(mainModule)
        }
        install(Authentication) {
            jwt("auth-jwt") {
                realm = "myRealm"
                verifier(
                    JWT
                        .require(Algorithm.HMAC256("secret"))
                        .withAudience("audience")
                        .withIssuer("issuer")
                        .build()
                )
                validate { credential ->
                    if (credential.payload.getClaim("username").asString() != "") {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }
            }
        }
        configureSession()
        configureSockets()
        configureRouting()

        configureSerialization()


    }.start(wait = true)
}
