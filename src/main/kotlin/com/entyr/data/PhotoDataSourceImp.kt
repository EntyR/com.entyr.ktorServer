package com.entyr.data

import com.entyr.data.table.ImagesTable
import com.entyr.model.SavePhotoRequest
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region.US_EAST_1
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

class PhotoDataSourceImp: PhotoDataSource {
    val awsCreds = AwsBasicCredentials.create(
        "AKIAXIIFRSPMGKS6LPFE",
        "qHx89xSyOQnnKM0BC1KzB0soC2O2iKrm2ENp8te+"
    )

    val s3: S3Client = S3Client.builder()
        .region(US_EAST_1)
        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
        .build()

    override suspend fun addPhoto(savePhotoRequest: SavePhotoRequest): String {

        val objectRequest = PutObjectRequest.builder()
            .bucket("test-bucket-asdwa")
            .key(savePhotoRequest.key)
            .build()

        val getUrlRequest = GetUrlRequest.builder()
            .bucket("test-bucket-asdwa")
            .key(savePhotoRequest.key)
            .build()

        val re = s3.putObject(objectRequest, RequestBody.fromFile(savePhotoRequest.file))
        val url = s3.utilities().getUrl(getUrlRequest).toString()
        transaction {
            ImagesTable.insert { table ->
                table[ImagesTable.uri] = url

            }
        }
        return url

    }

    override suspend fun getPhoto(key: String): String {
        val getUrlRequest = GetUrlRequest.builder()
            .bucket("test-bucket-asdwa")
            .key(key)
            .build()
        val url = s3.utilities().getUrl(getUrlRequest).toString()
        return url
    }
}