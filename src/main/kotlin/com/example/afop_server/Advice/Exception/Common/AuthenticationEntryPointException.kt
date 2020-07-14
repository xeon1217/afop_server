package com.example.afop_server.Advice.Exception.Common

class AuthenticationEntryPointException : RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}