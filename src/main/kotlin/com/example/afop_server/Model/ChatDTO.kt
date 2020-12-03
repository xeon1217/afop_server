package com.example.afop_server.Model

/**
 * 채팅에 사용 될 데이터 모델
 */
data class ChatDTO (
        val type: MessageType,
        val roomID: String,
        val senderUID: String,
        var message: String
) {
    enum class MessageType {
        ENTER, CHAT
    }
}