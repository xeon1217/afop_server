package com.example.afop_server.Model

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.awt.print.Book
import java.util.*
import java.util.stream.Collectors
import javax.persistence.*

@Entity
@Table(name = "user")
class User (private val email: String,
            private val password: String,
            private val name: String,
            private val nickName: String,
            private val code: Long,
            @ElementCollection(fetch = FetchType.EAGER)
            private val roles: List<String>) : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
    private var enable: Boolean = false

    fun getPk(): Long? {
        return id
    }

    override fun getUsername(): String {
        return email
    }

    override fun getPassword(): String {
        return password
    }

    fun getName(): String {
        return name
    }

    fun getNickName(): String {
        return nickName
    }

    fun getCode(): Long {
        return code
    }

    fun getRole(): List<String> {
        return roles
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
        //return roles.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
        //.map(SimpleGrantedAuthority::new) 코틀린 대응 https://stackoverrun.com/ko/q/12453840
        //또는 .map(::SimpleGrantedAuthority)
    }

    override fun isEnabled(): Boolean { // 계정이 사용 가능한 계정인지
        return enable
    }

    fun activation() {
        enable = true
    }

    fun deActivation() {
        enable = false
    }

    override fun isCredentialsNonExpired(): Boolean { // 계정의 패스워드가 만료되지 않았는지
        return true
    }

    override fun isAccountNonExpired(): Boolean { // 계정이 만료되지 않았는지지
       return true
    }

    override fun isAccountNonLocked(): Boolean { // 계정이 잠겨있지 않은지
        return true
    }
}