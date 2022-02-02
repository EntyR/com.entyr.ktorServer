package com.entyr.model

import kotlinx.serialization.Serializable

@Serializable
data class LastMessageModel(
    val userName: String,
    val sendToUser: String,
    val userNickName: String,
    val sendToNickName: String,
    val userProfPick: String,
    val sendProfPick: String,
    val text: String,
    val timestamp: Long,
    val xCord: Long,
    val yCord: Long,
    val url: String = "",
    var reed: Boolean
)
