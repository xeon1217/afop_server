package com.example.afop_server.Controller

import com.example.afop_server.Model.ChatDTO
import com.example.afop_server.Model.ChatRoomDTO
import com.example.afop_server.Repository.ChatRoomRepository
import com.example.afop_server.Response.Result
import com.example.afop_server.Response.Response
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatRoomController(private val chatRoomRepository: ChatRoomRepository) {

    @GetMapping("/rooms")
    fun rooms(): Result<List<ChatRoomDTO>> {
        return Result(data = chatRoomRepository.findAll(), response = Response(success = true))
    }

    @GetMapping("/room")
    fun room(@RequestParam id: String): Result<ChatRoomDTO> {
        return Result(data = chatRoomRepository.findChatRoomDTOById(id), response = Response(success = true))
    }

    @PostMapping("/room")
    fun createRoom(@RequestParam uid: String): Result<ChatRoomDTO> {
        return Result(data = chatRoomRepository.save(ChatRoomDTO(member = mutableSetOf(uid))), response = Response(success = true))
    }
}