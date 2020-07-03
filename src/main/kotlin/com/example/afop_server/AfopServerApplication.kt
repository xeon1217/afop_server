package com.example.afop_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class AfopServerApplication
fun main(args: Array<String>) {
    runApplication<AfopServerApplication>(*args)
}
//https://velog.io/@minholee_93/Spring-Security-JWT-Security-Spring-Boot-10 설계
//https://codevang.tistory.com/267?category=857253 login Server 개념
//https://daddyprogrammer.org/post/1239/spring-oauth-authorizationserver/ //Oauth Server