package com.example.afop_server.Common

import java.util.*

class Log {
    companion object {
        var debug: Boolean = true

        fun d(tag: String?, msg: String) {
            if(debug) {
                println("${now()} :: Debug --- [${tag}] : ${msg}")
            }
        }

        fun i() {
            println("")
        }

        fun e() {
            println("")
        }

        fun now(): Date {
            return Date()
        }
    }
}