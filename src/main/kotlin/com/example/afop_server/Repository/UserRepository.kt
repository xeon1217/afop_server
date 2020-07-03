package com.example.afop_server.Repository

import com.example.afop_server.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository <User, Long> {
    fun findByEmail(email: String) : Optional<User>
}