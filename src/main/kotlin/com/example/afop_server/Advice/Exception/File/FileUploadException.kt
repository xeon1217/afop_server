package com.example.afop_server.Advice.Exception.File

class FileUploadException : RuntimeException {
    constructor(msg: String?, t: Throwable?) : super(msg, t) {}
    constructor(msg: String?) : super(msg) {}
    constructor() : super() {}
}