package com.example.afop_server.Advice

import com.example.afop_server.Advice.Exception.*
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun defaultException(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult(getMessage("unKnown.code").toInt(), getMessage("unKnown.title_msg"))
    }

    @ExceptionHandler(CHttpRequestMethodNotSupportedException::class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected fun cHttpRequestMethodNotSupportedException(request: HttpServletRequest, e: CHttpRequestMethodNotSupportedException): CommonResult {
        return responseService.getFailResult(getMessage("unKnown.code").toInt(), getMessage("unKnown.title_msg"))
    }

    @ExceptionHandler(CAuthenticationEntryPointException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected fun cAuthenticationEntryPointException(request: HttpServletRequest, e: CAuthenticationEntryPointException): CommonResult {
        return responseService.getFailResult(getMessage("entryPointException.code").toInt(), getMessage("entryPointException.title_msg"))
    }

    @ExceptionHandler(CUserNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun cUserNotFoundException(request: HttpServletRequest, e: CUserNotFoundException): CommonResult {
        return responseService.getFailResult(getMessage("userNotFound.code").toInt(), getMessage("userNotFound.title_msg"), getMessage("userNotFound.do_msg"))
    }

    @ExceptionHandler(CSigninFailedException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun cSigninFailedException(request: HttpServletRequest, e: CSigninFailedException): CommonResult {
        return responseService.getFailResult(getMessage("signinFailed.code").toInt(), getMessage("signinFailed.title_msg"), getMessage("signinFailed.do_msg"))
    }

    @ExceptionHandler(CAlreadyUserEmailException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun cAlreadyUserEmailException(request: HttpServletRequest, e: CAlreadyUserEmailException): CommonResult {
        return responseService.getFailResult(getMessage("alreadyUserEmail.code").toInt(), getMessage("alreadyUserEmail.title_msg"), getMessage("alreadyUserEmail.do_msg"))
    }

    @ExceptionHandler(CAlreadyUserNickNameException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun cAlreadyUserNickNameException(request: HttpServletRequest, e: CAlreadyUserNickNameException): CommonResult {
        return responseService.getFailResult(getMessage("alreadyUserNickName.code").toInt(), getMessage("alreadyUserNickName.title_msg"), getMessage("alreadyUserNickName.do_msg"))
    }

    @ExceptionHandler(CEmptyDataException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun cEmptyDataException(request: HttpServletRequest, e: CEmptyDataException): CommonResult {
        return responseService.getFailResult(getMessage("emptyData.code").toInt(), getMessage("emptyData.title_msg"), getMessage("emptyData.do_msg"))
    }

    @ExceptionHandler(CSignUpUserException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun cSignUpUserException(request: HttpServletRequest, e: CSignUpUserException): CommonResult {
        return responseService.getFailResult(getMessage("signupUser.code").toInt(), getMessage("signupUser.title_msg"), getMessage("signupUser.do_msg"))
    }

    @ExceptionHandler(CWrongCodeException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun cWrongCodeException(request: HttpServletRequest, e: CWrongCodeException): CommonResult {
        return responseService.getFailResult(getMessage("wrongCode.code").toInt(), getMessage("wrongCode.title_msg"), getMessage("wrongCode.do_msg"))
    }

    @ExceptionHandler(CExpiredUserException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun cExpiredUserException(request: HttpServletRequest, e: CExpiredUserException): CommonResult {
        return responseService.getFailResult(getMessage("wrongCode.code").toInt(), getMessage("wrongCode.title_msg"), getMessage("wrongCode.do_msg"))
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