package com.example.afop_server.Advice.Exception

//리소스에 접근할 수 있는 권한이 없음
class CAuthenticationEntryPointException : RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}