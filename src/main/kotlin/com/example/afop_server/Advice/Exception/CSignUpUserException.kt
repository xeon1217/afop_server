package com.example.afop_server.Advice.Exception

//회원가입중인 유저(코드 입력이 완려되지 않음)가 로그인을 시도
class CSignUpUserException : RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}