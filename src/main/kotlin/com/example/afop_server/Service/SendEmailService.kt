package com.example.afop_server.Service

import com.example.afop_server.Model.MailDTO
import com.example.afop_server.Repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class SendEmailService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var mailSender: JavaMailSender
    private val FROM_ADDRESS = "xeon1217@gmail.com"

    fun registerMail(address: String, verifyAdress: String) {
        mailSend(createMail(
                address = address,
                title = "인증해주세요",
                message = "링크를 눌러 인증을 완료합니다.\n" + verifyAdress
        ))
    }

    fun createMail(address: String, title: String, message: String): MailDTO {
        return MailDTO(
                address = address,
                title = title,
                message = message
        )
    }

    fun mailSend(mail: MailDTO) {
        SimpleMailMessage().apply {
            setTo(mail.address)
            setFrom(FROM_ADDRESS)
            setSubject(mail.title)
            setText(mail.message)
            mailSender.send(this)
        }
    }
}