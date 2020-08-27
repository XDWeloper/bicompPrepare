package ru.xdweloper.bicompPreparer.utils

import com.andreapivetta.kolor.green
import com.andreapivetta.kolor.lightBlue
import com.andreapivetta.kolor.lightGreen
import com.andreapivetta.kolor.red
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 *@created 25 Август 2020 - 16:48
 *@project bicompPrepare
 *@author XDWeloper
 */

enum class MessageType {ERROR,INFO, DEBUG}


object  Message {
    fun info(message: String){
        print(message,MessageType.INFO)
    }

    fun error(message: String){
        print(message,MessageType.ERROR)
    }

    fun debug(message: String){
        print(message,MessageType.DEBUG)
    }

        fun print(message: String, messageType: MessageType) {
            when (messageType) {
                MessageType.INFO -> {println(message.lightGreen())
                                        getXLogger("All pojo").info(message)
                                        }
                MessageType.ERROR -> {println(message.red())
                                        getXLogger("All pojo").error(message)
                                        }
                MessageType.DEBUG -> {println(message.lightBlue())
                                        getXLogger("All pojo").debug(message)
                                        }
            }

        }
}

inline fun getXLogger(className: String?) = LoggerFactory.getLogger(className)

fun buildPath(vararg strList: String) = buildString{
    for((index,str) in strList.withIndex()){
        val s = if(index != (strList.size - 1))
            File.separator
        else ""
            append(str).append(s)
    }
}

fun exec(cmd: String, stdIn: String = "", captureOutput:Boolean = false, workingDir: File = File(".")): String? {
    try {
        val process = ProcessBuilder(*cmd.split("\\s".toRegex()).toTypedArray())
            .directory(workingDir)
            .redirectOutput(if (captureOutput) ProcessBuilder.Redirect.PIPE else ProcessBuilder.Redirect.INHERIT)
            .redirectError(if (captureOutput) ProcessBuilder.Redirect.PIPE else ProcessBuilder.Redirect.INHERIT)
            .start().apply {
                if (stdIn != "") {
                    outputStream.bufferedWriter().apply {
                        write(stdIn)
                        flush()
                        close()
                    }
                }
                waitFor(10, TimeUnit.MINUTES)
            }
        if (captureOutput) {
            return process.inputStream.bufferedReader().readText()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}