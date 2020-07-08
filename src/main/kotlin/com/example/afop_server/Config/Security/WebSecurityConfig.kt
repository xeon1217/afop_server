package com.example.afop_server.Config.Security

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
class WebSecurityConfig(private val jwtTokenProvider: JwtTokenProvider) : WebSecurityConfigurerAdapter() {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity?) {
        http?.let {
            it
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests() // 리소스들의 접근 권한 설정
                    .antMatchers("/auth/**").permitAll() // 인증 관련 리소스(로그인, 회원가입 등)에 접근을 허가함
                    .antMatchers(HttpMethod.GET, "/i18n/exception/**").permitAll() // 예외 관련 리소스에 접근을 허가함
                    .anyRequest().hasRole("USER") // 그 외의 리소스는 해당 권한(User)이 있어야 접근 가능
                    .and()
                    .exceptionHandling().authenticationEntryPoint(CAuthenticationEntryPoint())
                    .and()
                    .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
        }
    }
}