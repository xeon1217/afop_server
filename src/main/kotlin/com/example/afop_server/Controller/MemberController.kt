package com.example.afop_server.Controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * 회원 정보 관련
 * 회원의 정보를 수정하거나 탈퇴 등을 담당하는 Controller
 */

@RestController
@RequestMapping("/member")
class MemberController {
    fun changePassword() {

    }

    fun changeNickname() {

    }

    fun changeEmail() {

    }
}