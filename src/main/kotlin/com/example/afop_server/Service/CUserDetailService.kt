package com.example.afop_server.Service

import com.example.afop_server.Advice.Exception.CUserNotFoundException
import com.example.afop_server.Repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CUserDetailService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByEmail(username).orElseThrow(::CUserNotFoundException)
    }
}