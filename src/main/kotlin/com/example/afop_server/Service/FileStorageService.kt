package com.example.afop_server.Service

import com.example.afop_server.Advice.Exception.File.FileDownloadException
import com.example.afop_server.Advice.Exception.File.FileUploadException
import com.example.afop_server.Config.FileStorageProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageService {

    private val fileStorageLocation: Path

    @Autowired
    constructor(properties: FileStorageProperties) {
        fileStorageLocation = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize()
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (e: Exception) {
            throw FileUploadException()
        }
    }

    //파일 업로드
    fun storeFile(file: MultipartFile): String {
        val fileName: String = StringUtils.cleanPath(file.originalFilename!!)

        try {
            if(fileName.contains("..")) {
                throw FileUploadException()
            }

            val targetLocation: Path = fileStorageLocation.resolve(fileName)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            return fileName
        } catch (e: Exception) {
            throw FileUploadException()
        }
    }

    //파일 다운로드
    fun loadFileAsResource(fileName: String): Resource {
        try {
            val filePath: Path = fileStorageLocation.resolve(fileName).normalize()
            val resource: Resource = UrlResource(filePath.toUri())

            if(resource.exists()) {
                return resource;
            }else {
                throw FileDownloadException()
            }
        } catch (e: Exception) {
            throw FileDownloadException()
        }
    }
}