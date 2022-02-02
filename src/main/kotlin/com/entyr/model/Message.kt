package com.entyr.model

import kotlinx.serialization.Serializable
import java.sql.Timestamp
@Serializable
data class Message(
    val userName: String,
    val sendToUser: String,
    val text: String,
    val timestamp: Long,
    val xCord: Long,
    val yCord: Long,
    val url: String = "",
    var reed: Boolean
)
