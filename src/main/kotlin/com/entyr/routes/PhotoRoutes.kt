package com.entyr.routes

import com.entyr.data.PhotoDataSource
import com.entyr.model.Post
import com.entyr.model.Response
import com.entyr.model.SavePhotoRequest
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.cio.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File
import java.lang.Exception

fun Route.photoRoutes(
    photo: PhotoDataSource
){
    authenticate("auth-jwt") {

        post("/upload-image") {

            val savePhotoRequest: SavePhotoRequest = SavePhotoRequest(null, null)

            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                when (part){
                    is PartData.FileItem -> {
                        val fileBytes = part.streamProvider().readBytes()
                        val filename = part.originalFileName as String
                        val file = File(filename)
                        file.writeBytes(fileBytes)
                        savePhotoRequest.file = file
                    }
                    is PartData.FormItem -> {
                        savePhotoRequest.key = part.value
                    }
                    else -> Unit
                }

                part.dispose()
            }
            if (savePhotoRequest.key != null || savePhotoRequest.file != null) {
                val url = photo.addPhoto(savePhotoRequest)
                call.respond(HttpStatusCode.OK, Response(true, url))
            } else call.respond(HttpStatusCode.BadRequest)


        }
    }
}