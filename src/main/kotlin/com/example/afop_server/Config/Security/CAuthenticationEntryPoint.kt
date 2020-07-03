package com.example.afop_server.Config.Security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val exception: String = "/i18n/exception"
    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
        response?.sendRedirect("$exception/entrypoint")
    }
}