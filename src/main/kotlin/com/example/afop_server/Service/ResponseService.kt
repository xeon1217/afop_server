package com.example.afop_server.Service

import com.example.afop_server.Response.CommonResult
import com.example.afop_server.Response.ListResult
import com.example.afop_server.Response.SingleResult
import org.springframework.stereotype.Service

@Service
class ResponseService {
    enum class CommonResponse(val code: Int, val msg: String) {
        SUCCESS(0, "성공"),
        FAIL(-1, "실패");
    }

    fun <T> getSingleResult(data: T): SingleResult<T> {
        return SingleResult(data, CommonResult(true, CommonResponse.SUCCESS.code, CommonResponse.SUCCESS.msg))
    }

    fun <T> getListResult(list: List<T>): ListResult<T> {
        return ListResult(list, CommonResult(true, CommonResponse.SUCCESS.code, CommonResponse.SUCCESS.msg))
    }

    fun getSuccessResult(): CommonResult {
        return CommonResult(true, CommonResponse.SUCCESS.code, CommonResponse.SUCCESS.msg)
    }

    fun getFailResult(): CommonResult {
        return CommonResult(false, CommonResponse.FAIL.code, CommonResponse.FAIL.msg)
    }

    fun getFailResult(code: Int, msg: String): CommonResult {
        return CommonResult(false, code, msg)
    }
}