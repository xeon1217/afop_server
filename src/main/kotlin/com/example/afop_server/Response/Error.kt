package com.example.afop_server.Response

data class Error(
        val status: Int,
        val code: String,
        val message: String
)