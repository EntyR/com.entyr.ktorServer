package com.entyr.model

import io.ktor.auth.*

data class User(
    val email: String,
    val hashPassword: String,
    val username: String
)