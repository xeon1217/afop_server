package com.example.afop_server.Repository

import com.example.afop_server.Model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository <User?, Long> {
    fun findByEmail(email: String) : User?
    fun findByNickName(nickName: String) : User?
}

// https://shlee0882.tistory.com/257 JPA CURD 관련