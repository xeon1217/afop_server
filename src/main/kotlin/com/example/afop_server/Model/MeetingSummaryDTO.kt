package com.example.afop_server.Model

data class MeetingSummaryDTO (
        val id: String, //모임 id -> Key
        val seq: Long, //글 순서
        val logo: String, //모임 로고
        val title: String, //모임 이름
        val members: Long, //멤버 수
        val type: MeetingDTO.Type //모입 타입
)