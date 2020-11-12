package com.example.afop_server.Controller

import com.example.afop_server.Advice.Exception.Common.AccessDeniedException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 예외 관련
 * 각종 예외들을 담당하는 Controller
 */

@RestController
@RequestMapping("/exception")
class ExceptionController {
    @GetMapping("/access-denied")
    fun entrypointException() {
        throw AccessDeniedException()
    }
}