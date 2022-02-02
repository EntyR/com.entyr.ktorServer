package com.entyr.data

import com.entyr.model.SavePhotoRequest
import java.io.File

interface PhotoDataSource {

    suspend fun addPhoto(photo: SavePhotoRequest): String

    suspend fun getPhoto(key: String): String


}