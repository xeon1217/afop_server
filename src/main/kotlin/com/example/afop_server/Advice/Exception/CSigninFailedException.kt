package com.example.afop_server.Advice.Exception

//로그인에 실패함
class CSigninFailedException : RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}