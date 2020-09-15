package ru.xdweloper.bicompPreparer

import ru.xdweloper.bicompPreparer.fileWorkers.PropFileReader
import ru.xdweloper.bicompPreparer.mainProcess.startProcessing
import ru.xdweloper.bicompPreparer.utils.Message
import ru.xdweloper.bicompPreparer.utils.MessageType
import java.util.*

/**
 *@created 25 Август 2020 - 16:48
 *@project bicompPrepare
 *@author XDWeloper
 */


fun main(args: Array<String>) {

    val propReader = PropFileReader()
    propReader.propFileList.forEach {
        Message.print("$_SERACHED_PROPFILE ${it.propFileName} $_Q_SERACHED_PROPFILE",MessageType.INFO)
        if(Scanner(System.`in`).next().also { Message.debug(it) }.toLowerCase() == "y"){
            startProcessing(it)
            }
    }
}





