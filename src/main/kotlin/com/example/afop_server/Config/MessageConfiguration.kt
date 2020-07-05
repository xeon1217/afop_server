package com.example.afop_server.Config

import net.rakugakibox.util.YamlResourceBundle
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*
import java.util.Locale
import java.util.MissingResourceException


@Configuration
class MessageConfiguration : WebMvcConfigurer {
    @Bean // 세션에 지역설정. default는 KOREAN = 'ko'
    fun localeResolver(): LocaleResolver? {
        val slr = SessionLocaleResolver()
        slr.setDefaultLocale(Locale.KOREAN)
        return slr
    }

    @Bean // 지역설정을 변경하는 인터셉터. 요청시 파라미터에 lang 정보를 지정하면 언어가 변경됨.
    fun localeChangeInterceptor(): LocaleChangeInterceptor? {
        val lci = LocaleChangeInterceptor()
        lci.paramName = "lang"
        return lci
    }

    // 인터셉터를 시스템 레지스트리에 등록
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeChangeInterceptor()!!)
    }

    @Bean // yml 파일을 참조하는 MessageSource 선언
    fun messageSource(
            @Value("\${spring.messages.basename}") basename: String?,
            @Value("\${spring.messages.encoding}") encoding: String?
    ): MessageSource? {
        val ms = YamlMessageSource()
        ms.setBasename(basename!!)
        ms.setDefaultEncoding(encoding)
        ms.setAlwaysUseMessageFormat(true)
        ms.setUseCodeAsDefaultMessage(true)
        ms.setFallbackToSystemLocale(true)
        return ms
    }

    // locale 정보에 따라 다른 yml 파일을 읽도록 처리
    private class YamlMessageSource : ResourceBundleMessageSource() {
        @Throws(MissingResourceException::class)
        override fun doGetBundle(basename: String, locale: Locale): ResourceBundle {
            return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE)
        }
    }
}