package com.example.afop_server.Response

data class ListResult<T> (val list: List<T>, val result: CommonResult)