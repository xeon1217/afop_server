package com.example.afop_server.Config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "file")
class FileStorageProperties {
    private lateinit var uploadDir: String

    fun getUploadDir(): String {
        return uploadDir
    }

    fun setUploadDir(_uploadDir: String) {
        uploadDir = _uploadDir
    }
}