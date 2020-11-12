package com.example.afop_server.Controller

import com.example.afop_server.Advice.Exception.Auth.UserNotFoundException
import com.example.afop_server.Advice.Exception.Auth.WrongPasswordException
import com.example.afop_server.Advice.Exception.Common.EmptyDataException
import com.example.afop_server.Repository.UserRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

/**
 * 회원 정보 관련
 * 회원의 정보를 수정하거나 탈퇴 등을 담당하는 Controller
 */

@RestController
@RequestMapping("/member")
class MemberController(private val passwordEncoder: PasswordEncoder, private val userRepository: UserRepository) {

    /*
    @RequestMapping(path = ["/password"], method = [RequestMethod.PATCH])
    fun changePassword(@RequestBody body: Map<String, String>): CommonResult {
        val oPassword = body["oPassword"] // 원래 패스워드
        val password = body["password"] // 기준 패스워드
        val rPassword = body["rPassword"] // 재 확인 패스워드
        val authentication: Authentication = SecurityContextHolder.getContext().authentication

        //값이 유효한지 검사
        if (oPassword.isNullOrEmpty() || password.isNullOrEmpty() || rPassword.isNullOrEmpty()) {
            throw EmptyDataException()
        }

        userRepository.findByEmail(authentication.name)?.let { user ->
            if(!passwordEncoder.matches(oPassword, user.password)) {
                throw Exception() // 기존 패스워드가 맞지 않음!
            }

            if(password != rPassword) {
                throw WrongPasswordException() // 기준 패스워드와 확인 패스워드가 일치하지 않음!
            }

            //user.password = passwordEncoder.encode(password)
            //user.credentialsActivation()
            userRepository.save(user)
            return responseService.getSuccessResult()
        }
        throw UserNotFoundException()
    }

    @RequestMapping(path = ["/nickname"], method = [RequestMethod.PATCH])
    fun changeNickname(@RequestParam nickName: String): CommonResult {
        return responseService.getSuccessResult()
    }

     */
}