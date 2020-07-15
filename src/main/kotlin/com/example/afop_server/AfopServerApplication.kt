package com.example.afop_server

import com.example.afop_server.Config.GracefulShutdown
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Bean

@SpringBootApplication
class AfopServerApplication {
    @Bean
    fun gracefulShutdown(): GracefulShutdown {
        return GracefulShutdown()
    }

    @Bean
    fun webServerFactory(gracefulShutdown: GracefulShutdown): ConfigurableServletWebServerFactory {
        val factory: TomcatServletWebServerFactory = TomcatServletWebServerFactory()
        factory.addConnectorCustomizers(gracefulShutdown)
        return factory
    }
}


fun main(args: Array<String>) {
    runApplication<AfopServerApplication>(*args)
}
//https://velog.io/@minholee_93/Spring-Security-JWT-Security-Spring-Boot-10 설계
//https://codevang.tistory.com/267?category=857253 login Server 개념
//https://daddyprogrammer.org/post/1239/spring-oauth-authorizationserver/ //Oauth Server