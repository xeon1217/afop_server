package com.example.afop_server.Model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors
import javax.persistence.*

/*
@Entity
@Table(name = "user")
data class UserDAO(
        var email: String,
        var password: String,
        var name: String,
        var nickName: String,
        var createDate: Long,
        var authCode: String,
        @ElementCollection(fetch = FetchType.EAGER)
        private var roles: List<String>
) : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
    private var enable: Boolean = false //계정이 만료되었는지? (회원가입 중인지 아닌지를 판별)
    private var credentials: Boolean = true //패스워드가 만료되었는지?
    private var tokenCode: String = ""

    fun getPk(): Long {
        return id!!
    }

    override fun getPassword(): String {
        return password
    }

    fun setPassword(_password: String) {
        password = _password
    }

    fun getName(): String {
        return name
    }

    fun getNickName(): String {
        return nickName
    }

    fun getCreateDate(): Long {
        return createDate
    }

    fun getCode(): String {
        return authCode
    }

    fun setCode(_code: String) {
        authCode = _code
    }

    fun getRole(): List<String> {
        return roles
    }

    fun setRole(_roles: List<String>) {
        roles = _roles
    }

    fun getTokenCode(): String {
        return tokenCode
    }

    fun setTokenCode(_code: String) {
        tokenCode = _code
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

    override fun getUsername(): String {
        return username
    }

    fun activation() {
        enable = true
    }

    fun deActivation() {
        enable = false
    }

    override fun isCredentialsNonExpired(): Boolean { // 계정의 패스워드가 만료되지 않았는지
        return credentials
    }

    fun credentialsActivation() {
        credentials = true
    }

    fun credentialsDeActivation() {
        credentials = false
    }

    override fun isAccountNonExpired(): Boolean { // 계정이 만료되지 않았는지
        return true
    }

    override fun isAccountNonLocked(): Boolean { // 계정이 잠겨있지 않은지
        return true
    }
}

 */