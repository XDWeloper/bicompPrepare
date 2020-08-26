package ru.xdweloper.bicompPreparer

import ru.xdweloper.bicompPreparer.fileWorkers.ChangeFileReader
import ru.xdweloper.bicompPreparer.fileWorkers.PropFileReader
import ru.xdweloper.bicompPreparer.utils.getXLogger
import java.util.*

/**
 *@created 25 Август 2020 - 16:48
 *@project bicompPrepare
 *@author XDWeloper
 */


fun main(args: Array<String>) {
    var logger = getXLogger("main")

    var inputStr : String? = null
    val propReader = PropFileReader()
    val changesList = ChangeFileReader()


    val reader = Scanner(System.`in`)
    while (!inputStr.equals("exit")) {
            inputStr = reader.next()
            logger.info(_INPUT_TEXT + inputStr)
        }
    }



