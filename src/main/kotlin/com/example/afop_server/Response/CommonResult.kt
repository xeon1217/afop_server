package com.example.afop_server.Response

data class CommonResult (
        val success: Boolean, //응답 성공 여부 : t/f
        val code: Int, //응답 코드 번호 : >= 0 정상, < 0 비정상
        val msg: String //응답 메시지
)