package com.example.afop_server.Config.Security

import com.example.afop_server.Service.CUserDetailService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
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
class JwtTokenProvider (private val userDetailsService: CUserDetailService){
    @Value("\${spring.jwt.secret}")
    private lateinit var secretKey: String //Key
    private val tokenValidTime: Long = 1000L * 60 * 60 * 12 //token 유효시간 12시간
    private val changePasswordTokenValidTime: Long = 1000L * 600 //token 유효시간 10분

    @PostConstruct
    fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    }

    fun createToken(userPk: String, roles: List<String>): String {
        val claims: Claims = Jwts.claims().setSubject(userPk)
        claims["roles"] = roles
        val now = Date()

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

    fun createChangePasswordToken(userPk: String, roles: List<String>): String {
        val claims: Claims = Jwts.claims().setSubject(userPk)
        claims["roles"] = roles
        val now = Date()

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + changePasswordTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

    fun getUserPk(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(this.getUserPk(token))
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