package com.example.afop_server.Controller

import com.example.afop_server.Advice.Exception.CAlreadyUserException
import com.example.afop_server.Advice.Exception.CAuthenticationEntryPointException
import com.example.afop_server.Advice.Exception.CSigninFailedException
import com.example.afop_server.Advice.Exception.CUserNotFoundException
import com.example.afop_server.Common.Log
import com.example.afop_server.Config.Security.JwtTokenProvider
import com.example.afop_server.Model.User
import com.example.afop_server.Repository.UserRepository
import com.example.afop_server.Response.CommonResult
import com.example.afop_server.Response.SingleResult
import com.example.afop_server.Service.ResponseService
import org.springframework.http.HttpStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * 회원 인증 관련
 * 회원의 회원가입 및 로그인 과정을 담당하는 Controller
 */

@RestController
@RequestMapping("/auth")
class AuthController(private val passwordEncoder: PasswordEncoder, private val jwtTokenProvider: JwtTokenProvider, private val userRepository: UserRepository, private val responseService: ResponseService) {
    private val tag = AuthController::class.simpleName

    @PostMapping("/signin")
    fun signin(@RequestBody user: Map<String, String>): SingleResult<String> {
        val email = user["email"]
        val password = user["password"]

        if(email.isNullOrEmpty() || password.isNullOrEmpty()) {
            Log.d(tag, "이메일 또는 비밀번호가 비어있음")
            throw CUserNotFoundException()
        }

        val user: User = userRepository.findByEmail(email).orElseThrow(::CUserNotFoundException)
        if(!user.isEnabled) {
            Log.d(tag, "인증 중인 계정")
            throw CAuthenticationEntryPointException() // 인증중인 계정입니다!
        }
        if(!passwordEncoder.matches(password, user.password)) {
            throw CSigninFailedException()
        }
        return responseService.getSingleResult(jwtTokenProvider.createToken("${user.getPk()}", user.getRole()))
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun signup(@RequestBody user: Map<String, String>): CommonResult {
        val email = user["email"]
        val password = user["password"]
        val name = user["name"]
        val nickName = user["nickName"]

        if(email.isNullOrEmpty() || password.isNullOrEmpty() || name.isNullOrEmpty() || nickName.isNullOrEmpty()) {
            throw CUserNotFoundException()
        }
        if(!userRepository.findByEmail(email).isEmpty or !userRepository.findByNickName(nickName).isEmpty) {
            throw CAlreadyUserException()
        }
        val code = Date().time
        userRepository.save(User(email, passwordEncoder.encode(password), name, nickName, code, Collections.singletonList("USER")))
        println(code)
        return responseService.getSuccessResult()
    }

    @RequestMapping(path = ["/signup/{email}"], method = [RequestMethod.PUT])
    fun certify(@PathVariable("email") email: String, @RequestBody data: Map<String, Long>): CommonResult {
        val code = data["code"]
        val user: User = userRepository.findByEmail(email).orElseThrow(::CUserNotFoundException)

        if(user.isEnabled){
           throw CAuthenticationEntryPointException() // 활성화되어있는 유저
        }
        if(user.getCode() != code) {
            throw Exception() // 코드가 다를 경우
        }
        user.activation()
        userRepository.save(user)
        return responseService.getSuccessResult()
    }

    @RequestMapping(path = ["/signup/{email}"], method = [RequestMethod.GET])
    fun doubleCheckEmail(@PathVariable("email") email: String): CommonResult {
        if(email.isEmpty()) {
            throw CUserNotFoundException() // 입력이 비어있음
        }

        if(!userRepository.findByEmail(email).isEmpty) {
            throw CAlreadyUserException()
        }
        return responseService.getSuccessResult()
    }
}