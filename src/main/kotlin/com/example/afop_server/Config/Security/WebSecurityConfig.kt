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
                    .antMatchers(HttpMethod.GET, "/exception/**").permitAll() // 예외 관련 리소스에 접근을 허가함
                    .antMatchers(HttpMethod.PATCH,"/auth/password").hasRole("PW") // 비 로그인한 유저의 패스워드 변경은 토큰을 발급받아야 함
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/market/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/image/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/files/**").permitAll()
                    .antMatchers("/chat/**").permitAll() // 임시
                    .antMatchers("/pub/**").permitAll() // 임시
                    .antMatchers("/sub/**").permitAll() // 임시
                    .antMatchers("/ws-stomp/**").permitAll() // 임시
                    //.antMatchers("/market/**").permitAll()
                    .anyRequest().authenticated() // 그 외의 리소스는 인증된 회원만 사용 가능
                    .and()
                    .exceptionHandling().authenticationEntryPoint(CAuthenticationEntryPoint())
                    .and()
                    .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
        }
    }
}