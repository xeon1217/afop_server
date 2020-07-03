package com.example.afop_server.Controller

import com.example.afop_server.Advice.Exception.CAuthenticationEntryPointException
import com.example.afop_server.Response.CommonResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/i18n/exception")
class ExceptionController {
    @GetMapping("/entrypoint")
    fun entrypointException(): CommonResult {
        println("sadsadas")
        throw CAuthenticationEntryPointException("asdsad")
    }
}