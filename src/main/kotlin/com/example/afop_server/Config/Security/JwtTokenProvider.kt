package com.example.afop_server.Config.Security

import com.example.afop_server.Advice.Exception.Auth.WrongCodeException
import com.example.afop_server.Advice.Exception.Common.AuthenticationEntryPointException
import com.example.afop_server.Common.Log
import com.example.afop_server.Model.User
import com.example.afop_server.Service.CUserDetailService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.awt.print.Book
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(private val userDetailsService: CUserDetailService) {
    @Value("\${spring.jwt.secret}")
    private lateinit var secretKey: String //Key
    private val tokenValidTime: Long = 1000L * 60 * 60 * 48 //token 유효시간 48시간
    private val changePasswordTokenValidTime: Long = 1000L * 60 * 30//패스워드 변경 token 유효시간 30분

    @PostConstruct
    fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    }

    fun createToken(user: User): String {
        val now = Date()
        val claims: Claims = Jwts.claims()
                .setSubject(user.getPk().toString())
                .setId(user.getTokenCode())
        claims["roles"] = user.getRole()

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

    fun createChangePasswordToken(user: User): String {
        val now = Date()
        val claims: Claims = Jwts.claims()
                .setSubject(user.getPk().toString())
                .setId(user.getTokenCode())
        claims["roles"] = user.getRole()

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + changePasswordTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

    fun getUserPk(token: String): String {
        println(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject)
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun getTokenCode(token: String): String {
        println(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.id)
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.id
    }

    fun getAuthentication(token: String): Authentication? {
        val userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token))
        if (userDetails.getTokenCode() != this.getTokenCode(token)) {
            return null
        }
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader("X-AUTH-TOKEN")
    }

    fun validDateToken(jwtToken: String): Boolean {
        return try {
            val claims: Jws<Claims> = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}