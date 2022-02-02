package com.entyr.data

import com.entyr.auth.Authentification
import com.entyr.data.table.UsersTable
import com.entyr.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UsersImplementation(val jwt: Authentification): Users {
    override suspend fun addUser(request: RegisterRequest): String {


        transaction {
            UsersTable.insert { table ->
                table[UsersTable.email] = request.email
                table[UsersTable.name] = request.name
                table[UsersTable.hashPassword] = request.password
            }
        }
        return jwt.generateToken(
            User(
            request.email,
            request.password,
            request.name
        )
        )

    }

    override suspend fun loginUser(loginRequest: LoginRequest): String? {
        return transaction {
            val resultRow = UsersTable.select {
                UsersTable.email.eq(loginRequest.email) and
                        UsersTable.hashPassword.eq(loginRequest.password)
            }.firstOrNull()
            val user = resultRow?.let {
                User(
                    it[UsersTable.email],
                    it[UsersTable.hashPassword],
                    it[UsersTable.name]
                )
            }
            user?.let {
                jwt.generateToken(it)
            }
        }
    }



    override suspend fun getUsersByName(name: String, mark: Int): List<UserInfo> {
        return transaction {
            UsersTable.select {
                UsersTable.name.eq(name)
            }.map {
                UserInfo(
                    it[UsersTable.email],
                    it[UsersTable.name]
                )
            }
        }
    }

    override suspend fun getUsers(mark: Int): List<UserInfo> {
        return transaction {
            UsersTable.selectAll().map {
                UserInfo(

                    it[UsersTable.email],
                    it[UsersTable.name],
                    it[UsersTable.profilePic],
                    it[UsersTable.desc]
                )
            }
        }
    }

    override suspend fun getUserInfo(user: String): UserInfo? {
        return transaction {
            val user = UsersTable.select(
                UsersTable.email.eq(user)
            ).singleOrNull()


            user?.let {
                UserInfo(
                    user[UsersTable.email],
                    user[UsersTable.name],
                    user[UsersTable.profilePic],
                    user[UsersTable.desc]
                )
            }



        }
    }

    override suspend fun updateUserInfo(userInfo: UserInfo) {
        transaction {
            UsersTable.update ({
                UsersTable.email.eq(userInfo.email)
            } ){ tb ->
                tb[UsersTable.name] = userInfo.username
                tb[UsersTable.profilePic] = userInfo.profPic
                tb[UsersTable.desc] = userInfo.desc
            }
        }
    }
}