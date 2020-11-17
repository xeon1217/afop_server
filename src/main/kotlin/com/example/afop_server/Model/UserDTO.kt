package com.example.afop_server.Model

/**
 * 유저 데이터를 사용하기 위한 데이터 모델
 */
data class UserDTO(
        var uid: String, //서버에서 제공되는 유저의 Key
        var fcmToken: String?, //서버에서 제공되는 유저의 FCM Token
        var token: String, //JWT 토큰
        var email: String, //유저의 이메일 주소
        var name: String, //유저의 이름
        var nickname: String //유저의 닉네임
)