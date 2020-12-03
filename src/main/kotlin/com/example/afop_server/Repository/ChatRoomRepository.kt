package com.example.afop_server.Repository

import com.example.afop_server.Model.ChatRoomDTO
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository : JpaRepository<ChatRoomDTO, String> {
    fun findChatRoomDTOById(id: String): ChatRoomDTO
}