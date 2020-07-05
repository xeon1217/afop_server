package com.example.afop_server.Controller

import com.example.afop_server.Advice.Exception.CAlreadyUserException
import com.example.afop_server.Advice.Exception.CSigninFailedException
import com.example.afop_server.Advice.Exception.CUserNotFoundException
import com.example.afop_server.Config.Security.JwtTokenProvider
import com.example.afop_server.Model.User
import com.example.afop_server.Repository.UserRepository
import com.example.afop_server.Response.CommonResult
import com.example.afop_server.Response.SingleResult
import com.example.afop_server.Service.ResponseService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1")
class UserController(private val passwordEncoder: PasswordEncoder, private val jwtTokenProvider: JwtTokenProvider, private val userRepository: UserRepository, private val responseService: ResponseService) {
    @PostMapping("/signin")
    fun signin(@RequestBody user: Map<String, String>): SingleResult<String> {
        val email = user["email"]
        val password = user["password"]
        if(email.isNullOrEmpty() || password.isNullOrEmpty()) {
            throw CUserNotFoundException()
        }

        val user: User = userRepository.findByEmail(email).orElseThrow(::CSigninFailedException)
        if(!passwordEncoder.matches(password, user.password)) {
            throw CSigninFailedException()
        }
        return responseService.getSingleResult(jwtTokenProvider.createToken("${user.getPk()}", user.getRole()))
    }

    @PostMapping("/signup")
    fun signup(@RequestBody user: Map<String, String>): CommonResult {
        val email = user["email"]
        val password = user["password"]
        val name = user["name"]
        val nickName = user["nickName"]
        if(email.isNullOrEmpty() || password.isNullOrEmpty() || name.isNullOrEmpty() || nickName.isNullOrEmpty()) {
            throw CUserNotFoundException()
        }
        if(!userRepository.findByEmail(email).isEmpty) {
            throw CAlreadyUserException()
        }
        if(!userRepository.findByNickName(nickName).isEmpty) {
            throw CAlreadyUserException()
        }
        userRepository.save(User(email, passwordEncoder.encode(password), name, nickName, Collections.singletonList("USER")))
        return responseService.getSuccessResult()
    }

    @GetMapping("/nickname")
    fun checkNickName(@RequestBody user: Map<String, String>): CommonResult {
        val nickName = user["nickName"]
        if(nickName.isNullOrEmpty()) {
            throw CUserNotFoundException()
        }
        if(!userRepository.findByNickName(nickName).isEmpty) {
            throw CAlreadyUserException()
        }
        return responseService.getSuccessResult()
    }

    @GetMapping("/test")
    fun test(): String {
        return Date().toString()
    }
}