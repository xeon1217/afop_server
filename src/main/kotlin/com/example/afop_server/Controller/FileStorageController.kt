package com.example.afop_server.Controller

import com.example.afop.data.result.Response
import com.example.afop.data.result.Result
import com.example.afop_server.Service.FileStorageService
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
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

    @GetMapping("/{fileName}")
    fun downloadFile(@PathVariable fileName: String, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Resource> {
        val resource: Resource = service.loadFileAsResource(fileName = fileName)
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

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
                .body(resource)
    }
}