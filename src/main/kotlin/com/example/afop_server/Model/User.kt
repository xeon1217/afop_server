package com.example.afop_server.Model

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors
import javax.persistence.*

@Entity
@Table(name = "user")
class User (private val email: String,
            private val password: String,
            private val name: String,
            private val nickName: String,
            @ElementCollection(fetch = FetchType.EAGER)
            private val roles: List<String>) : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

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

    fun getRole(): List<String> {
        return roles
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
        //.map(SimpleGrantedAuthority::new) 코틀린 대응 https://stackoverrun.com/ko/q/12453840
        //또는 .map(::SimpleGrantedAuthority)
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}