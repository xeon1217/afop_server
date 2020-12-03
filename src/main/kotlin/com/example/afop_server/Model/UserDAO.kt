package com.example.afop_server.Model

import org.hibernate.annotations.GenericGenerator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
@Table(name = "user")
class UserDAO(
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(columnDefinition = "CHAR(36)")
        private val id: String = "",
        private val email: String,
        private val password: String,
        var name: String,
        var nickName: String,
        var role: String,
        var tokenCode: String? = null,
        var fcmToken: String? = null,
        var createTimeStamp: Long = Date().time,
        var verifyEmail: Boolean = false
) : UserDetails {
    //계정의 primaryKey를 반환함
    fun getID() = id

    //계정의 이름을 반환
    override fun getUsername() = email

    //계정의 비밀번호를 반환
    override fun getPassword() = password

    //계정이 갖고있는 권한 목록을 반환
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = ArrayList<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(role))
        return authorities
        //return roles.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
    }

    //계정이 활성화되어 있는지?
    override fun isEnabled() = true

    //계정의 비밀번호가 만료되지 않는지?
    override fun isCredentialsNonExpired() = true

    //계정이 만료되지 않는지?
    override fun isAccountNonExpired() = true

    //계정이 잠겨있지 않는지?
    override fun isAccountNonLocked() = true

    //이메일 인증을 마친 상태인지?
    fun isVerifyEmail() = verifyEmail
}