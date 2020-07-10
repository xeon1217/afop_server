package com.example.afop_server.Response

class ListResult<T> (val list: List<T>, success: Boolean, code: Int, title_msg: String, do_msg: String) : CommonResult(success, code, title_msg, do_msg)