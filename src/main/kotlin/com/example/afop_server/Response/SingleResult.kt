package com.example.afop_server.Response

class SingleResult<T> (val data: T, success: Boolean, code: Int, title_msg: String, do_msg: String) : CommonResult(success, code, title_msg, do_msg)