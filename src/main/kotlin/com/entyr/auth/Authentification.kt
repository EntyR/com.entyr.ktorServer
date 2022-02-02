package com.entyr.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.entyr.model.User

class Authentification {

    fun generateToken(user: User):String {
        val token = JWT.create()
            .withAudience("audience")
            .withIssuer("issuer")
            .withClaim("username", user.email)
            .sign(Algorithm.HMAC256("secret"))
        return token
    }

}