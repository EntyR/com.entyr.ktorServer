package com.entyr.model

import io.ktor.http.cio.websocket.*

data class SessionMember(
    val userEmail: String,
    val sessionId: String,
    val socket: WebSocketSession
)