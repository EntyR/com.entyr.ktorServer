package com.entyr.model

data class UserInfo(
    val email: String,
    val username: String,
    val profPic: String = "",
    val desc: String = ""
)
