package com.entyr.data

import com.entyr.model.Post
import com.entyr.model.PostResponse

interface PostDataSource {

    suspend fun createPost(post: Post)

    suspend fun getAllPosts(mark: Post? = null): List<PostResponse>

    suspend fun getSubscribedPosts(email: String,  mark: Post? = null): List<Post>
}