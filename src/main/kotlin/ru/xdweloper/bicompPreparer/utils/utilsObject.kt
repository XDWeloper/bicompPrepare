package ru.xdweloper.bicompPreparer.utils

import com.andreapivetta.kolor.green
import com.andreapivetta.kolor.lightBlue
import com.andreapivetta.kolor.red
import org.slf4j.LoggerFactory

/**
 *@created 25 Август 2020 - 16:48
 *@project bicompPrepare
 *@author XDWeloper
 */

enum class MessageType {ERROR,INFO, DEBUG}


object  Message {
        fun print(message: String, messageType: MessageType) {
            when (messageType) {
                MessageType.INFO -> println(message.green())
                MessageType.ERROR -> println(message.red())
                MessageType.DEBUG -> println(message.lightBlue())
            }

        }
}

inline fun getXLogger(className: String?) = LoggerFactory.getLogger(className)


