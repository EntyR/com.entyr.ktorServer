package com.entyr.model

data class Post(
    val id: Int?,
    val author: String,
    val url: String,
    val date: Long,
    val text: String,
    val title: String,
    val where: String,
    val xCord: Long,
    val yCord: Long
    )
