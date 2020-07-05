package com.example.afop_server.Advice.Exception

//유저가 존재하지 않음
class CUserNotFoundException : RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}