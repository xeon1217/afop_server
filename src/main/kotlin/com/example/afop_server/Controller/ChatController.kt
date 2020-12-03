package com.example.afop_server.Controller

import com.example.afop_server.Model.ChatDTO
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(private val simpMessageSendingOperations: SimpMessageSendingOperations) {

    @MessageMapping("/chat/message")
    fun message(chatDTO: ChatDTO) {
        if (chatDTO.type == ChatDTO.MessageType.ENTER) {
            chatDTO.message += "${chatDTO.senderUID}님이 입장하셨습니다."
        }
        println()
        simpMessageSendingOperations.convertAndSend("/sub/chat/room/${chatDTO.roomID}", chatDTO)
    }
}