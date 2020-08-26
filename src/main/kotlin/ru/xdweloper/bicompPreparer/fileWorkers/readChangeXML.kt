package ru.xdweloper.bicompPreparer.fileWorkers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File

/**
 *@created 26 Август 2020 - 14:24
 *@project bicompPrepare
 *@author XDWeloper
 */

class ChangeFileReader{

    var changesData = null

    constructor(){
        //testxml()

        readChangesFile()
        }

//    private fun testxml() {
//        val changes = Changes("bvz")
//        val xmlMapper = XmlMapper()
//        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)
//        xmlMapper.writeValue(File("c:\\Temp\\BicompWorker\\source\\changes.xml"), changes)
//    }

    private fun readChangesFile() {
        val obj = convertXmlFile2DataObject("c:\\Temp\\BicompWorker\\source\\change.xml",Changes::class.java)
        val o = 0
    }


        fun convertXmlFile2DataObject(pathFile: String, cls: Class<*>): Any{
            val xmlMapper = XmlMapper()
            return xmlMapper.readValue(File(pathFile), cls)
        }
    }



