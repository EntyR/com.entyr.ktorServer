package com.entyr.di

import com.entyr.auth.Authentification
import com.entyr.chat.ChatController
import com.entyr.data.*
import com.entyr.data.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module

val mainModule = module {

    single(createdAtStart = true) {
        //For heroku deployment
//        val uri = URI(System.getenv("DATABASE_URL"))
//        val username = uri.userInfo.split(":").toTypedArray()[0]
//        val password = uri.userInfo.split(":").toTypedArray()[1]
//        val fullUrl = "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path + "?sslmode=require" + "&user=$username&password=$password"

        Database.connect(

            url = "jdbc:postgresql:message_db?user=postgres&password=69420",
            driver = "org.postgresql.Driver"
        )
        transaction {
            SchemaUtils.create(UsersTable)
            SchemaUtils.create(ImagesTable)
            SchemaUtils.create(PostTable)
            SchemaUtils.create(FollowerTable)
            SchemaUtils.create(SocketChatTable)
        }
    }

    single {
        Authentification()
    }

    single<FollowerDataSource> {
        FollowerDataSourceImp()
    }
    single<MessageDataSource> {
        MessageDataSourceImp()
    }
    single<PhotoDataSource> {
        PhotoDataSourceImp()
    }
    single<PostDataSource> {
        PostsDataSourceImp()
    }
    single<Users> {
        UsersImplementation(get())
    }
    single {
        ChatController(get())
    }


}