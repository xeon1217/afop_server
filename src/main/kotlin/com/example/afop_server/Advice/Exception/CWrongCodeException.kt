package com.example.afop_server.Advice.Exception

//잘못된 코드 입력
class CWrongCodeException : RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}