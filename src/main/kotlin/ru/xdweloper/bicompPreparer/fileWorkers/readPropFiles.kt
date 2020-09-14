package ru.xdweloper.bicompPreparer.fileWorkers

import ru.xdweloper.bicompPreparer.*
import ru.xdweloper.bicompPreparer.utils.Message
import ru.xdweloper.bicompPreparer.utils.buildPath
import java.io.File

/**
 *@created 25 Август 2020 - 16:48
 *@project bicompPrepare
 *@author XDWeloper
 */


class PropFileReader{
    val workDir = System.getProperty("user.dir")
    val propFileList = arrayListOf<PropFile>()
    var propReadState  = ResaltState.OK
    var propReaderError: String? = null


    init {
        loadAllPropFiles()
    }

    private fun loadAllPropFiles() {
        Message.info("$_READ_FILES $workDir")
        File(workDir)
            .walkTopDown()
            .filter { it.isFile && it.extension.equals("properties") }
            .filter { it.path.substringBeforeLast(File.separator) == workDir }
            .forEach {
                Message.info("$_SEARCHED_FILE $it")
                getPropFromFile(it)?.let { it1 -> propFileList.add(it1) }
            }
    }

    private fun getPropFromFile(propFile: File): PropFile? {
        return propFile?.let{
            val propText = it.readLines()
            Message.info("$_READED_PROP_TEXT  $propText")
            return@let findFromPropText(propText, "readDir")?.let { it1 ->
                findFromPropText(propText, "workDir")?.let { it2 ->
                    findFromPropText(propText, "sourceDir")?.let { it3 ->
                        findFromPropText(propText, "changeFile")?.let { it4 ->
                            findFromPropText(propText, "projectName")?.let { it5 ->
                                findFromPropText(propText, "pom")?.let { it6 ->
                                    findFromPropText(propText, "mavenDir")?.let { it7 ->
                                        PropFile(
                                            readDir = it1,
                                            workDir = it2,
                                            sourceDir = it3,
                                            changeFile = it4,
                                            projectName = it5,
                                            propFileName = it.name,
                                            pom = it6,
                                            mavenDir = it7
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun findFromPropText(propTextList: List<String>?, searchProp: String): String? {
        return propTextList?.let {
            it.forEach {
                if(it.contains(searchProp, true)){
                    val propResult: String? = it.split("=")[1]
                    Message.info("$_FINDED_PROP_VALUE  $searchProp : $propResult")
                    return@let propResult
                }
            }
            return@let ""
        }
    }
}

data class PropFile(val readDir:String = "",
                    val workDir:String = "",
                    val sourceDir:String = "",
                    val changeFile:String = "",
                    val projectName: String = "",
                    val propFileName: String = "",
                    val pom: String = "",
                    val mavenDir:String = ""){

    fun checkProps(): Pair<ResaltState,String>{
        //проверим наличие свойств
        if(readDir.isNullOrBlank() || readDir.isNullOrEmpty()) return Pair(ResaltState.ERROR,"$_NOT_PROP_VALUE readDir")
        if(workDir.isNullOrBlank() || workDir.isNullOrEmpty()) return Pair(ResaltState.ERROR,"$_NOT_PROP_VALUE workDir")
        if(sourceDir.isNullOrBlank() || sourceDir.isNullOrEmpty()) return Pair(ResaltState.ERROR,"$_NOT_PROP_VALUE sourceDir")
        if(changeFile.isNullOrBlank() || changeFile.isNullOrEmpty()) return Pair(ResaltState.ERROR,"$_NOT_PROP_VALUE changeFile")
        if(projectName.isNullOrBlank() || projectName.isNullOrEmpty()) return Pair(ResaltState.ERROR,"$_NOT_PROP_VALUE projectName")
        if(mavenDir.isNullOrBlank() || mavenDir.isNullOrEmpty()) return Pair(ResaltState.ERROR,"$_NOT_PROP_VALUE mavenDir")
        if(pom.isNullOrBlank() || pom.isNullOrEmpty()) return Pair(ResaltState.ERROR,"$_NOT_PROP_VALUE pom")

        //проверим наличие соответствующих путей и файлов
        if(!File(readDir).isDirectory || !File(readDir).exists()) return Pair(ResaltState.ERROR,"$_CHECK_PROP_DATA_PATH readDir")
        if(!File(workDir).isDirectory || !File(workDir).exists())  return Pair(ResaltState.ERROR,"$_CHECK_PROP_DATA_PATH workDir")
        if(!File(sourceDir).isDirectory || !File(sourceDir).exists())  return Pair(ResaltState.ERROR,"$_CHECK_PROP_DATA_PATH sourceDir")
        if(!File(mavenDir).isDirectory || !File(mavenDir).exists())  return Pair(ResaltState.ERROR,"$_CHECK_PROP_DATA_PATH mavenDir")

        if(!File(buildPath(sourceDir,changeFile)).isFile  || !File(buildPath(sourceDir,changeFile)).exists())  return Pair(ResaltState.ERROR,"$_CHECK_PROP_DATA_FILE changeFile")
        if(!File(pom).isFile  || !File(pom).exists()) return Pair(ResaltState.ERROR,"$_CHECK_PROP_DATA_FILE pom")

        return Pair(ResaltState.OK,"")
    }
}


