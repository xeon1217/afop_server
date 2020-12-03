package com.example.afop_server.Advice

import com.example.afop_server.Advice.Exception.Auth.*
import com.example.afop_server.Advice.Exception.Common.AccessDeniedException
import com.example.afop_server.Advice.Exception.Common.EmptyDataException
import com.example.afop_server.Advice.Exception.File.FileDownloadException
import com.example.afop_server.Advice.Exception.File.FileUploadException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest
import com.example.afop_server.Response.Result
import com.example.afop_server.Response.ErrorCode

@RestControllerAdvice
class  ExceptionAdvice {
    // 공통
    @ExceptionHandler(AccessDeniedException::class)
    protected fun accessDeniedException(request: HttpServletRequest, e: AccessDeniedException): Result<*> {
        return Result(data = null, error = ErrorCode.ACCESS_DENIED)
     }

    @ExceptionHandler(EmptyDataException::class)
    protected fun emptyDataException(request: HttpServletRequest, e: EmptyDataException): Result<*> {
        return Result(data = null, error = ErrorCode.EMPTY_DATA)
    }

    @ExceptionHandler(Exception::class)
    protected fun exception(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.NOT_DEFINE_ERROR)
    }

    @ExceptionHandler(AlreadyUserEmailException::class)
    protected fun alreadyUserEmailException(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.ALREADY_USER_EMAIL)
    }

    @ExceptionHandler(AlreadyUserNickNameException::class)
    protected fun alreadyUserNickNameException(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.ALREADY_USER_NICKNAME)
    }

    @ExceptionHandler(RegisteringUserException::class)
    protected fun registeringUserException(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.REGISTERING_USER)
    }

    @ExceptionHandler(WrongPasswordException::class)
    protected fun wrongPasswordException(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.WRONG_PASSWORD)
    }

    @ExceptionHandler(ExpiredVerifyEmailException::class)
    protected fun expiredVerifyEmailException(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.EXPIRED_VERIFY_EMAIL)
    }

    @ExceptionHandler(NotVerifyEmailException::class)
    protected fun notVerifyEmailException(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.NOT_VERIFY_EMAIL)
    }

    @ExceptionHandler(ExpiredTokenException::class)
    protected fun expiredTokenException(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.EXPIRED_TOKEN)
    }

    @ExceptionHandler(FailedLoginException::class)
    protected fun failedLoginException(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.FAILED_LOGIN)
    }

    @ExceptionHandler(FileUploadException::class)
    protected fun fileUploadException(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.FILE_UPLOAD_FAIL)
    }

    @ExceptionHandler(FileDownloadException::class)
    protected fun fileDownloadException(request: HttpServletRequest, e: Exception): Result<*> {
        return Result(data = null, error = ErrorCode.FILE_DOWNLOAD_FAIL)
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