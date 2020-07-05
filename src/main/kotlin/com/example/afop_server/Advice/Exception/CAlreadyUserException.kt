package com.example.afop_server.Advice.Exception

//이미 존재하는 계정
class CAlreadyUserException : RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}