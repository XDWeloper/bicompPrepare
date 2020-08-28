package ru.xdweloper.bicompPreparer

import ru.xdweloper.bicompPreparer.fileWorkers.PropFile
import ru.xdweloper.bicompPreparer.utils.Message
import ru.xdweloper.bicompPreparer.utils.buildPath
import ru.xdweloper.bicompPreparer.utils.exec
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


/**
 *@created 27 Август 2020 - 11:10
 *@project bicompPrepare
 *@author XDWeloper
 */


object mainProcess {

    fun startProcessing(propFile: PropFile): Boolean {
        /** Проверим наличие путей и файлов*/
        if (!checkPropFile(propFile)) return false
        Message.info("$_CHECK_PROP_SUCSESS")


        if (propFile.projectName.equals("FXbicomp.jar", true))
            startBicompProcessing(propFile).takeUnless { it }?.let { return it }

        return true
    }

    private fun startBicompProcessing(propFile: PropFile): Boolean {
        val kernelFileList: MutableList<Triple<String,File,String>> = mutableListOf()
        val kernelOkFileList: MutableList<Pair<ResaltState,Triple<String,File,String>>> = mutableListOf()

        /**проверяем наличие  FRREPRunner.java*/
        checkFRREPruner(propFile).takeUnless { it }?. let { return false }
        /**Проверим наличие архиватора*/
        checkArch().takeUnless { it }?. let { return false }
        /**Ищем архивный файл(ы) с KERNEL*/
        checkKernelFiles(propFile, kernelFileList as ArrayList<Triple<String,File,String>>).takeUnless { it }?.let { return false }

        kernelFileList?.let {
            it.forEach { currentFile ->
                unArchProject(propFile,currentFile).takeIf { it }?.let {
                    /**здесь нужно еще проверить наличие FXBicomp*/
                    searchTargetFile(currentFile,propFile).takeIf { it }?.let {
                        kernelOkFileList.add(Pair(ResaltState.OK,currentFile))
                    }
                }
            }
        }



        return true
    }

    private fun searchTargetFile(currentFile:Triple<String,File,String>,propFile: PropFile): Boolean {
        val fullTargetFilePath = buildPath(propFile.workDir,currentFile.first,"Client","JAPP",propFile.projectName)
        return File(fullTargetFilePath).exists().also {
            Message.debug(fullTargetFilePath)
            if(it) Message.debug("${propFile.projectName} найден")
            else   Message.debug("${propFile.projectName} не найден")
        }
    }

    inline private fun checkArch(): Boolean {
        File("7z.exe")
            .exists()
            .takeUnless { it }
            ?.let { Message.error("$_NOT_ARJ")
                     return false }
        return true
    }

    inline private fun checkFRREPruner(propFile: PropFile): Boolean {
        Message.info("$_CHECK_ADD_FILE - FRREPRunner.java")
        File(propFile.sourceDir?.let { buildPath(it, "FRREPRunner.java") }).exists()
            .takeUnless { it }?.let {
                Message.error("$_NOT_ADD_FILE - FRREPRunner.java")
                return false }
        return true
    }

    private fun unArchProject(propFile: PropFile, kernelFilesPair: Triple<String,File,String>): Boolean {
        val kernelFiles = kernelFilesPair.second
        val versionDir = kernelFilesPair.first
        val command = "7z.exe x -y -r ${buildPath(propFile.readDir!!,kernelFiles.name)} " +
                                        " \"-o${buildPath(propFile.workDir,versionDir)}\""

        File(buildPath(propFile.workDir,versionDir)).exists().takeIf { it }?.let {
            Message.debug("Проект версии $versionDir существует. Все равно обработать?(y/n)")
            if(Scanner(System.`in`).next().also { Message.debug(it) }.toLowerCase() == "n") {
                return false
            }
        }

        Message.debug(command)
        Message.debug("Wait........")
        val result = exec(cmd = command,captureOutput = true)
        result?.let { it->
            it.takeUnless { it.contains("Everything is Ok") }?.let{
                Message.error("$_ERROR_ARJ $it")
                return false
            }
        }
        Message.info("$_SUCSESS_ARJ")
        return true
    }

    private fun checkKernelFiles(propFile: PropFile,kernelFileList: ArrayList<Triple<String,File,String>>) : Boolean {
        Message.info("$_FIND_PROJECT_FILE - KERNEL....")
        File(propFile.readDir)
            .walkTopDown()
            .filter { it.isFile && it.extension.equals("ex") && it.name.contains("KERNEL") }
            .forEach {
                it?.let {
                    val fileVersion = it.name.substring(1,11)
                    Message.info("$_SEARCHED_FILE $it")
                    kernelFileList.add(Triple(fileVersion,it,""))
                }
            }

        kernelFileList.size.takeIf { p -> p == 0 }?.let {
            Message.error("$_NOT_PROJECT_FILE")
            return false
        }
        return true
    }

    private fun checkPropFile(propFile: PropFile): Boolean {
        Message.info("$_CHECK_PROP_FILE ${propFile.propFileName}")
        return  propFile.checkProps().let {
            it.also {
                if(it.first.equals(ResaltState.ERROR)){
                    Message.error("Ошибка: ${it.second}")
                }
            }
            it.first.equals(ResaltState.OK)
        }
    }
}




