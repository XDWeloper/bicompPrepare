package ru.xdweloper.bicompPreparer.fileWorkers

import ru.xdweloper.bicompPreparer.*
import ru.xdweloper.bicompPreparer.utils.getXLogger
import java.io.File

/**
 *@created 25 Август 2020 - 16:48
 *@project bicompPrepare
 *@author XDWeloper
 */


class PropFileReader{
    val logger = getXLogger(PropFileReader::class.java.toString())
    val workDir = System.getProperty("user.dir")
    val propFileList = arrayListOf<PropFile>()
    var propReadState: ReaderState = ReaderState.OK
    var propReaderError: String? = null


    init {
        loadAllPropFiles()
    }

    private fun loadAllPropFiles() {
        logger.info("$_READ_FILES $workDir")
        File(workDir)
            .walkTopDown()
            .filter { it.isFile && it.extension.equals("properties") }
            .forEach {
                logger.info("$_SEARCHED_FILE $it")
                getPropFromFile(it)?.let { it1 -> propFileList.add(it1) }
            }
    }

    private fun getPropFromFile(propFile: File): PropFile? {
        return propFile?.let{
            val propText = it.readLines()
            logger.info("$_READED_PROP_TEXT  $propText")
            return@let PropFile(
                readDir = findFromPropText(propText, "readDir"),
                workDir = findFromPropText(propText, "workDir"),
                sourceDir = findFromPropText(propText, "sourceDir"),
                changeFile = findFromPropText(propText, "changeFile"),
                projectName = findFromPropText(propText, "projectName")
            )
        }
    }

    private fun findFromPropText(propTextList: List<String>?, searchProp: String): String? {
        return propTextList?.let {
            it.forEach {
                if(it.contains(searchProp, true)){
                    val propResult: String? = it.split("=")[1]
                    logger.info("$_FINDED_PROP_VALUE  $searchProp : $propResult")
                    return@let propResult
                }
            }
            return@let ""
        }
    }
}

data class PropFile(val readDir:String?,
                    val workDir:String?,
                    val sourceDir:String?,
                    val changeFile:String?,
                    val projectName: String?){}


