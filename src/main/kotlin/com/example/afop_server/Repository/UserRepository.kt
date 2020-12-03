package com.example.afop_server.Repository

import com.example.afop_server.Model.UserDAO
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository <UserDAO?, String> {
    fun findByEmail(email: String) : UserDAO?
    fun findByNickName(nickName: String) : UserDAO?
}

// https://shlee0882.tistory.com/257 JPA CURD 관련