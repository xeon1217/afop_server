package com.example.afop_server.Advice

import com.example.afop_server.Advice.Exception.Auth.*
import com.example.afop_server.Advice.Exception.Common.AuthenticationEntryPointException
import com.example.afop_server.Advice.Exception.Common.EmptyDataException
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
    // 공통
    @ExceptionHandler(AuthenticationEntryPointException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun authenticationEntryPointException(request: HttpServletRequest, e: AuthenticationEntryPointException): CommonResult {
        return responseService.getFailResult(getMessage("AuthenticationEntryPointException.code").toInt(), getMessage("AuthenticationEntryPointException.title_msg"), getMessage("AuthenticationEntryPointException.do_msg"))
    }

    @ExceptionHandler(EmptyDataException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun emptyDataException(request: HttpServletRequest, e: EmptyDataException): CommonResult {
        return responseService.getFailResult(getMessage("EmptyDataException.code").toInt(), getMessage("EmptyDataException.title_msg"), getMessage("EmptyDataException.do_msg"))
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun exception(request: HttpServletRequest, e: Exception): CommonResult {
        return responseService.getFailResult(getMessage("UnKnownException.code").toInt(), getMessage("UnKnownException.title_msg"), getMessage("UnKnownException.do_msg"))
    }

    // 인증관련
    @ExceptionHandler(AlreadyUserEmailException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun alreadyUserEmailException(request: HttpServletRequest, e: AlreadyUserEmailException): CommonResult {
        return responseService.getFailResult(getMessage("AlreadyUserEmailException.code").toInt(), getMessage("AlreadyUserEmailException.title_msg"), getMessage("AlreadyUserEmailException.do_msg"))
    }

    @ExceptionHandler(AlreadyUserNickNameException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun alreadyUserNickNameException(request: HttpServletRequest, e: AlreadyUserNickNameException): CommonResult {
        return responseService.getFailResult(getMessage("AlreadyUserNickNameException.code").toInt(), getMessage("AlreadyUserNickNameException.title_msg"), getMessage("AlreadyUserNickNameException.do_msg"))
    }

    @ExceptionHandler(CodeTimeoutException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun codeTimeoutException(request: HttpServletRequest, e: CodeTimeoutException): CommonResult {
        return responseService.getFailResult(getMessage("CodeTimeoutException.code").toInt(), getMessage("CodeTimeoutException.title_msg"), getMessage("CodeTimeoutException.do_msg"))
    }

    @ExceptionHandler(ExpiredPasswordException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun expiredPasswordException(request: HttpServletRequest, e: ExpiredPasswordException): CommonResult {
        return responseService.getFailResult(getMessage("ExpiredPasswordException.code").toInt(), getMessage("ExpiredPasswordException.title_msg"), getMessage("ExpiredPasswordException.do_msg"))
    }

    @ExceptionHandler(SignInFailedException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun signInFailedException(request: HttpServletRequest, e: SignInFailedException): CommonResult {
        return responseService.getFailResult(getMessage("SignInFailedException.code").toInt(), getMessage("SignInFailedException.title_msg"), getMessage("SignInFailedException.do_msg"))
    }

    @ExceptionHandler(SignUpUserException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun signUpUserException(request: HttpServletRequest, e: SignUpUserException): CommonResult {
        return responseService.getFailResult(getMessage("SignUpUserException.code").toInt(), getMessage("SignUpUserException.title_msg"), getMessage("SignUpUserException.do_msg"))
    }


    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun userNotFoundException(request: HttpServletRequest, e: UserNotFoundException): CommonResult {
        return responseService.getFailResult(getMessage("UserNotFoundException.code").toInt(), getMessage("UserNotFoundException.title_msg"), getMessage("UserNotFoundException.do_msg"))
    }

    @ExceptionHandler(WrongCodeException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun wrongCodeException(request: HttpServletRequest, e: WrongCodeException): CommonResult {
        return responseService.getFailResult(getMessage("WrongCodeException.code").toInt(), getMessage("WrongCodeException.title_msg"), getMessage("WrongCodeException.do_msg"))
    }

    @ExceptionHandler(WrongPasswordException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun wrongPasswordException(request: HttpServletRequest, e: WrongPasswordException): CommonResult {
        return responseService.getFailResult(getMessage("WrongPasswordException.code").toInt(), getMessage("WrongPasswordException.title_msg"), getMessage("WrongPasswordException.do_msg"))
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