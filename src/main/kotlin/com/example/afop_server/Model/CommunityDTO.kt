package com.example.afop_server.Model

data class CommunityDTO (
        val id: String,
        val image: List<String>,
        val timeStamp: Long,
        val content: String,
        val comment: List<CommentDTO>
)