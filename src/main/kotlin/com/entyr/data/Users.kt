package com.entyr.data

import com.entyr.model.LoginRequest
import com.entyr.model.RegisterRequest
import com.entyr.model.User
import com.entyr.model.UserInfo

interface Users {

    suspend fun addUser(request: RegisterRequest): String

    suspend fun loginUser(loginRequest: LoginRequest): String?


    suspend fun getUsersByName(name: String, mark: Int): List<UserInfo>

    suspend fun getUsers(mark: Int): List<UserInfo>

    suspend fun getUserInfo(user: String): UserInfo?


    suspend fun updateUserInfo(userInfo: UserInfo)
}