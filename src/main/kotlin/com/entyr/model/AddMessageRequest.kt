package com.entyr.model

import kotlinx.serialization.Serializable
import javax.print.attribute.standard.JobOriginatingUserName

@Serializable
data class AddMessageRequest(
    val sendToUser: String,
    val text: String,
    val timestamp: Long,
    val xCord: Long,
    val yCord: Long,
    val url: String = "",
    var reed: Boolean
){
    fun toMessage(userName: String): Message = Message(
        userName = userName,
        sendToUser,
        text,
        timestamp,
        xCord,
        yCord,
        url,
        reed
    )
}
