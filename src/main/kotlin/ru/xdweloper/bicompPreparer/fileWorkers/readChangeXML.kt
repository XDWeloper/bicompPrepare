package ru.xdweloper.bicompPreparer.fileWorkers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import ru.xdweloper.bicompPreparer.ResaltState
import ru.xdweloper.bicompPreparer._LADED_CHANGE_FILE
import ru.xdweloper.bicompPreparer._LADED_FILE_ERROR
import ru.xdweloper.bicompPreparer._SEARCH_CHANGE_FILE
import ru.xdweloper.bicompPreparer.utils.Message
import ru.xdweloper.bicompPreparer.utils.buildPath
import ru.xdweloper.bicompPreparer.utils.getXLogger
import java.io.File

/**
 *@created 26 Август 2020 - 14:24
 *@project bicompPrepare
 *@author XDWeloper
 */

class ChangeFileReader(val changeFileName: String, val changeFilePath: String){

    var changesData: Changes? = null
    var xmlReadState = ResaltState.OK
    var xmlReaderError: String? = null
    var filePath: String = ""

    init {
        filePath = buildPath(changeFilePath,changeFileName)
        Message.info("$_SEARCH_CHANGE_FILE $filePath")
        readChangesFile()
        }

    private fun readChangesFile() {
        try {
            changesData = convertXmlFile2DataObject(filePath,Changes::class.java) as Changes
            Message.info("$_LADED_CHANGE_FILE")
        }catch (e: Exception){
            xmlReaderError = e.message
            xmlReadState = ResaltState.ERROR
            Message.info("$_LADED_FILE_ERROR $xmlReaderError")
        }
    }

        fun convertXmlFile2DataObject(pathFile: String, cls: Class<*>): Any{
            val xmlMapper = XmlMapper()
            return xmlMapper.readValue(File(pathFile), cls)
        }
    }



