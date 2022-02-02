package com.entyr.data

interface FollowerDataSource {
    suspend fun addFollower(author: String, followerEmail: String)

    suspend fun deleteFollower(author: String, followerEmail: String)
}