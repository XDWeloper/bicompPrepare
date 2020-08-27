package ru.xdweloper.bicompPreparer

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
//    val changesList = ChangeFileReader("ChangeDate.xml", "c:\\Temp\\BicompWorker\\source")

    propReader.propFileList.forEach {

    }


    while (!inputStr.equals("exit")) {

            inputStr = Scanner(System.`in`).next()
            logger.info(_INPUT_TEXT + inputStr)
        }
    }



