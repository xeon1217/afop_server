package com.example.afop_server.Service

import com.example.afop_server.Advice.Exception.Auth.UserNotFoundException
import com.example.afop_server.Model.UserDAO
import com.example.afop_server.Repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CUserDetailService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDAO? {
        return userRepository.findById(username).orElseThrow(::UserNotFoundException)
    }
}