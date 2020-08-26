package ru.xdweloper.bicompPreparer

import org.slf4j.Logger
import ru.xdweloper.bicompPreparer.fileWorkers.PropFileReader
import ru.xdweloper.bicompPreparer.utils.getXLogger
import java.util.*

/**
 *@created 25 Август 2020 - 16:48
 *@project bicompPrepare
 *@author XDWeloper
 */


fun main(args: Array<String>) {
    var logger: Logger = getXLogger("main")

    var inputStr : String? = null
    val propReader = PropFileReader()
    val propFileList = propReader.propFileList


    val reader = Scanner(System.`in`)
    while (!inputStr.equals("exit")) {
            inputStr = reader.next()
            logger.info(_INPUT_TEXT + inputStr)
        }
    }



