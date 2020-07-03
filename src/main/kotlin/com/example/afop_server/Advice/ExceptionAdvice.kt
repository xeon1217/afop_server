package com.example.afop_server.Advice

import com.example.afop_server.Advice.Exception.CAuthenticationEntryPointException
import com.example.afop_server.Advice.Exception.CSigninFailedException
import com.example.afop_server.Advice.Exception.CUserNotFoundException
import com.example.afop_server.Response.CommonResult
import com.example.afop_server.Service.ResponseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ExceptionAdvice(private val responseService: ResponseService) {
    /*
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun defaultException(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult()
    }
     */

    @ExceptionHandler(CUserNotFoundException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun cUserNotFoundException(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult()
    }

    @ExceptionHandler(CSigninFailedException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun cEmailSigninFailedException(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult()
    }

    @ExceptionHandler(CAuthenticationEntryPointException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun cAuthenticationEntryPointException(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult()
    }
}