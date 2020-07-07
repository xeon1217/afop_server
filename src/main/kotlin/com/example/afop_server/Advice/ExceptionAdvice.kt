package com.example.afop_server.Advice

import com.example.afop_server.Advice.Exception.CAlreadyUserException
import com.example.afop_server.Advice.Exception.CAuthenticationEntryPointException
import com.example.afop_server.Advice.Exception.CSigninFailedException
import com.example.afop_server.Advice.Exception.CUserNotFoundException
import com.example.afop_server.Response.CommonResult
import com.example.afop_server.Service.ResponseService
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ExceptionAdvice(private val responseService: ResponseService, private val messageSource: MessageSource) {
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun defaultException(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult(getMessage("unKnown.code").toInt(), getMessage("unKnown.msg"))
    }

    @ExceptionHandler(CUserNotFoundException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun cUserNotFoundException(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult(getMessage("userNotFound.code").toInt(), getMessage("userNotFound.msg"))
    }

    @ExceptionHandler(CSigninFailedException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun cSigninFailedException(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult(getMessage("signinFailed.code").toInt(), getMessage("signinFailed.msg"))
    }

    @ExceptionHandler(CAuthenticationEntryPointException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun cAuthenticationEntryPointException(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult(getMessage("entryPointException.code").toInt(), getMessage("entryPointException.msg"))
    }

    @ExceptionHandler(CAlreadyUserException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected fun cAlreadyUserException(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult(getMessage("alreadyUser.code").toInt(), getMessage("alreadyUser.msg"))
    }

    private fun getMessage(code: String): String {
        return getMessage(code, null)
    }

    //args : 로케일 관련
    private fun getMessage(code: String, args: List<Any>?): String {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale())
    }
}

/**
 * 예측 가능한 예외 목록
 *
 * 토큰 관련
 * 토큰 데이터 손상
 * 토큰 만료
 * 토큰 유효성
 *
 * 로그인 관련
 * 로그인 데이터 손상
 * 아이디 틀림/없음
 * 비밀번호 틀림
 *
 * 회원가입 관련
 * 회원가입 데이터 손상
 * 이미 존재하는 아이디
 * 이미 존재하는 닉네임
 *
 * 사용자 정보 수정 관련
 * 사용자 정보 수정 데이터 손상
 * 패스워드 변경 -> 이전과 같은 패스워드
 * 이미 존재하는 닉네임
 *
 * 알 수 없는 예외 -> 일단 다 이쪽으로 때려박음
 *
 **/