package com.example.afop_server.Common

import java.util.*

class Log {
    companion object {
        var debug: Boolean = true

        fun debug(tag: String?, msg: String) {
            if(debug) {
                println("${now()} :: Debug --- [${tag}] : ${msg}")
            }
        }

        fun info(tag: String?, msg: String) {
            println("")
        }

        fun error(tag: String?, msg: String) {
            println("")
        }

        fun warn(tag: String?, msg: String) {
            println("${now()} :: Debug --- [${tag}] : ${msg}")
        }

        fun now(): Date {
            return Date()
        }
    }
}