package com.example.afop_server.Advice.Exception

//입력이 비어있음
class CEmptyDataException : RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}