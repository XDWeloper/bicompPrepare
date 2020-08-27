package ru.xdweloper.bicompPreparer.fileWorkers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import ru.xdweloper.bicompPreparer.ReaderState
import ru.xdweloper.bicompPreparer._LADED_CHANGE_FILE
import ru.xdweloper.bicompPreparer._LADED_FILE_ERROR
import ru.xdweloper.bicompPreparer._SEARCH_CHANGE_FILE
import ru.xdweloper.bicompPreparer.utils.getXLogger
import java.io.File

/**
 *@created 26 Август 2020 - 14:24
 *@project bicompPrepare
 *@author XDWeloper
 */

class ChangeFileReader(val changeFileName: String, val changeFilePath: String){

    val logger = getXLogger(ChangeFileReader::class.java.toString())
    var changesData: Changes? = null
    var xmlReadState: ReaderState = ReaderState.OK
    var xmlReaderError: String? = null
    var filePath: String = ""

    init {
        filePath = changeFilePath + File.separator +  changeFileName
        logger.info("$_SEARCH_CHANGE_FILE $filePath")
        readChangesFile()
        }

    private fun readChangesFile() {
        try {
            changesData = convertXmlFile2DataObject(filePath,Changes::class.java) as Changes
            logger.info("$_LADED_CHANGE_FILE")
        }catch (e: Exception){
            xmlReaderError = e.message
            xmlReadState = ReaderState.ERROR
            logger.info("$_LADED_FILE_ERROR $xmlReaderError")
        }
    }

        fun convertXmlFile2DataObject(pathFile: String, cls: Class<*>): Any{
            val xmlMapper = XmlMapper()
            return xmlMapper.readValue(File(pathFile), cls)
        }
    }



