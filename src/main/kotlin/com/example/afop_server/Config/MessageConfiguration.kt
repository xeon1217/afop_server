package com.example.afop_server.Config

import net.rakugakibox.util.YamlResourceBundle
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*


@Configuration
class MessageConfiguration : WebMvcConfigurer {
}