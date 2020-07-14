package com.example.afop_server.Controller

import com.example.afop_server.Advice.Exception.Common.AuthenticationEntryPointException
import com.example.afop_server.Response.CommonResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 예외 관련
 * 각종 예외들을 담당하는 Controller
 */

@RestController
@RequestMapping("/i18n/exception")
class ExceptionController {
    @GetMapping("/entrypoint")
    fun entrypointException(): CommonResult {
        throw AuthenticationEntryPointException()
    }
}