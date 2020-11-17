package com.example.afop_server.Advice.Exception.Auth

class ExpiredTokenException : RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}