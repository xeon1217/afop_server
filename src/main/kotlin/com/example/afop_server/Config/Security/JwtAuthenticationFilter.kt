package com.example.afop_server.Config.Security

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


class JwtAuthenticationFilter(private val jwtTokenProvider: JwtTokenProvider) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val token: String? = jwtTokenProvider.resolveToken(request as HttpServletRequest)
        if (token != null && jwtTokenProvider.validDateToken(token)) {
            val authentication: Authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain?.doFilter(request, response)
    }
}