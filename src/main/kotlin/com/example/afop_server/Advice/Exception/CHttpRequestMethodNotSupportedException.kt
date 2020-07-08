package com.example.afop_server.Advice.Exception

//클라이언트의 요청이 허용되지 않는 메소드인 경우
class CHttpRequestMethodNotSupportedException: RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}