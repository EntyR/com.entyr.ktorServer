package com.entyr.data

import com.entyr.data.table.FollowerTable
import com.entyr.data.table.PostTable
import com.entyr.data.table.UsersTable
import com.entyr.model.Post
import com.entyr.model.PostResponse
import com.entyr.model.UserInfo
import io.ktor.utils.io.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PostsDataSourceImp(): PostDataSource {
    override suspend fun createPost(post: Post) {
        transaction {
            PostTable.insert { table ->
                table[author] = post.author
                table[date] = post.date
                table[text] = post.text
                table[uri] = post.url
                table[where] = post.where
                table[title] = post.title
                table[xCord] = post.xCord
                table[yCord] = post.yCord

            }
        }
    }

    override suspend fun getAllPosts(mark: Post?): List<PostResponse> {

        val posts = transaction {

            var list = PostTable.selectAll()
                .sortedByDescending {
                    PostTable.id
                }

            mark?.let {
                val index = list.indexOfFirst {
                    it[PostTable.id] == mark.id
                }
                list = list.subList(index, list.size)
            }

            list.take(10).map { table ->
                Post(
                    id = table[PostTable.id],
                    author = table[PostTable.author],
                    date = table[PostTable.date],
                    text = table[PostTable.text],
                    url = table[PostTable.uri],
                    where = table[PostTable.where],
                    title = table[PostTable.title],
                    xCord = table[PostTable.xCord],
                    yCord = table[PostTable.yCord]
                )
            }

        }

        return  transaction {
            posts.map { post->
                val user = UsersTable.select {
                    UsersTable.email.eq(post.author)
                }.singleOrNull()?.let {
                    UserInfo(
                        it[UsersTable.email],
                        it[UsersTable.name],
                        it[UsersTable.profilePic],
                        it[UsersTable.desc]
                    )
                }
                PostResponse(
                    post.author,
                    user?.username?:"Guest",
                    user?.profPic?: "",
                    post.url,
                    post.date,
                    post.text,
                    post.title,
                    "",
                    1,
                    1

                )
            }
        }
    }

        override suspend fun getSubscribedPosts(email: String, mark: Post?): List<Post> {
        return transaction {

            val sub = FollowerTable.select {
                FollowerTable.follower.eq(email)
            }

            var list = PostTable.select {
                PostTable.author.inSubQuery(sub)
            }.sortedByDescending {
                    PostTable.id
                }

            mark?.let {
                val index = list.indexOfFirst {
                    it[PostTable.id] == mark.id
                }
                list = list.subList(index, list.size)
            }

            list.take(10).map { table ->
                Post(
                    id = table[PostTable.id],
                    author = table[PostTable.author],
                    date = table[PostTable.date],
                    text = table[PostTable.text],
                    url = table[PostTable.uri],
                    where = table[PostTable.where],
                    title = table[PostTable.title],
                    xCord = table[PostTable.xCord],
                    yCord = table[PostTable.yCord]
                )
            }

        }
    }

}