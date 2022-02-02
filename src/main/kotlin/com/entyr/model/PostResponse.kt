package com.entyr.model

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val author: String,
    val authorName: String,
    val profileUri: String,
    val url: String,
    val date: Long,
    val text: String,
    val title: String,
    val where: String,
    val xCord: Long,
    val yCord: Long
    )
