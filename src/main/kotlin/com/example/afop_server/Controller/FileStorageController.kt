package com.example.afop_server.Controller

import com.example.afop_server.Response.Result
import com.example.afop_server.Response.Response
import com.example.afop_server.Service.FileStorageService
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/files")
class FileStorageController {

    @Autowired
    private lateinit var service: FileStorageService

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): Result<*> {
        service.storeFile(file)
        return Result(data = null, response = Response(success = true))
    }

    @PostMapping("/uploads")
    fun uploadMultipleFiles(@RequestParam("files") files: ArrayList<MultipartFile>): Result<*> {
        files.forEach {
            uploadFile(it)
        }
        return Result(data = null, response = Response(success = true))
    }

    @RequestMapping(path = ["/download"], method = [RequestMethod.GET])
    fun downloadFile(@Param("file") file: String, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Resource> {

        println(file)
        val resource: Resource = service.loadFileAsResource(fileName = file)
        var contentType: String = "image/jpeg"
        try {
            contentType = request.servletContext.getMimeType(resource.file.absolutePath)
        } catch (e: Exception) {

        }
        if (contentType == "") {
            contentType = "application/octet-stream"
        }

        //response.contentType = "image/jpeg"
        //response.outputStream.write(resource.file.readBytes())

        println("$resource")
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
                .body(resource)
    }

    @RequestMapping(path = ["/downloads"], method = [RequestMethod.GET])
    fun downloadMultipleFiles(@Param("files") files: ArrayList<String>) {
        files.forEach {
            service.loadFileAsResource(it)
        }
    }
}