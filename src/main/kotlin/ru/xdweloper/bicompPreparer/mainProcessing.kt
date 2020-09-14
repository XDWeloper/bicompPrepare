package ru.xdweloper.bicompPreparer

import ru.xdweloper.bicompPreparer.fileWorkers.ChangeFileReader
import ru.xdweloper.bicompPreparer.fileWorkers.PropFile
import ru.xdweloper.bicompPreparer.utils.*
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

        /**проверяем наличие  FRREPRunner.java*/
        checkFRREPruner(propFile).takeUnless { it }?. let { return false }
        /**проверяем наличие  ApFileRunnerFr3.java*/
        checkApFileRunnerFr3(propFile).takeUnless { it }?. let { return false }

        /**Проверим наличие архиватора*/
        checkArch().takeUnless { it }?. let { return false }
        /**Ищем архивный файл(ы) с KERNEL*/
        checkKernelFiles(propFile, kernelFileList as ArrayList<Triple<String,File,String>>).takeUnless { it }?.let { return false }

        kernelFileList?.let {
            it.forEach { currentFile ->
                unArchProject(propFile,currentFile).takeIf { it }?.let {
                    /**здесь нужно еще проверить наличие FXBicomp*/
                    searchTargetFile(currentFile,propFile) { s, triple, propFile -> runCretateProject(s,triple,propFile)}
                }
            }
        }


        return true
    }


    private fun runCretateProject(fullTargetFilePath: String,currentFile:Triple<String,File,String>,propFile: PropFile) {

        Message.info("Распаковка проекта из файла $fullTargetFilePath")
        val unArchTergetDir = buildPath(propFile.workDir,currentFile.first,PREPARE_PROJECT_DIR_NAME)
        unArchFile(fullTargetFilePath,unArchTergetDir)

        Message.info("Запускаем формирование проекта из файла $fullTargetFilePath")
        createProject(currentFile,propFile)
    }

    /**Создаем папку проекта и копируем туда все необходимое*/
    private fun createProject(currentFile:Triple<String,File,String>,propFile: PropFile){
        val workCurrentVersionDir = buildPath(propFile.workDir,currentFile.first,PROJECT_DIR_NAME)
        val unArchTergetDir = buildPath(propFile.workDir,currentFile.first,PREPARE_PROJECT_DIR_NAME)
        val sourseVersionDir = currentFile.first
        val sourceDir = buildPath("src","main","java")
        val resourceDir = buildPath("src","main","resources", "ru")
        val kernelVersion = sourseVersionDir.substring(0,3).plus(LIBRARY_DIR_NAME)

        /**Папка копируем src*/
            Message.debug("Копируем файлы из ${buildPath(unArchTergetDir,"source")}")
            File(buildPath(unArchTergetDir,"source")).copyOnExist(File(buildPath(workCurrentVersionDir,sourceDir)))

        /**Добавить файлик inversionkavkaz\frrep\FRREPRunner.java*/
            Message.debug("Копируем файлы из ${buildPath(propFile.sourceDir, "FRREPRunner.java")}")
            File(buildPath(propFile.sourceDir, "FRREPRunner.java"))
                .copyOnExist(File(buildPath(workCurrentVersionDir,sourceDir,"ru","inversionkavkaz","frrep","FRREPRunner.java")))

        /**Добавить файлик inversionkavkaz\frrep\ApFileRunnerFr3.java*/
            Message.debug("Копируем файлы из ${buildPath(propFile.sourceDir, "ApFileRunnerFr3.java")}")
            File(buildPath(propFile.sourceDir, "ApFileRunnerFr3.java"))
                .copyOnExist(File(buildPath(workCurrentVersionDir,sourceDir,"ru","inversion","bicomp","fxreport","apmain","ApFileRunnerFr3.java")))

        /**Папка копируем в resources*/
            Message.debug("Копируем файлы из ${buildPath(unArchTergetDir,"ru")}")
                File(buildPath(unArchTergetDir,"ru")).copyOnExist(File(buildPath(workCurrentVersionDir,resourceDir)))
            deleteClassFile(workCurrentVersionDir, resourceDir)

        /**Копируем pom файл*/
            Message.debug("Копируем pom файл из $propFile.pom в ${buildPath(workCurrentVersionDir,"pom.xml")}")
            File(propFile.pom).copyOnExist(File(buildPath(workCurrentVersionDir,"pom.xml")))

        /**Нужно исправить pom файл*/
            val depString = JInvFore.getDepString("1.0.0","ru.inversion.jp", buildPath(propFile.sourceDir,kernelVersion))
            makePomFile(workCurrentVersionDir,depString)

        /**Внедряем все изменения*/
            insertChanges(currentFile,propFile).takeUnless { it }?.let { return }

        /**Собираем проект*/
            runPackage(workCurrentVersionDir,propFile,currentFile)


    }

    private fun insertChanges(currentFile: Triple<String, File, String>, propFile: PropFile): Boolean {
        val workCurrentVersionDir = buildPath(propFile.workDir,currentFile.first,PROJECT_DIR_NAME,"src")
        val changes = ChangeFileReader(propFile.changeFile, propFile.sourceDir)
        var isOk: Boolean = true
        changes.xmlReadState.takeUnless { it == ResaltState.OK }?.let { return false }

        changes.changesData?.file?.forEach { it1 ->
            it1.modification.forEach { it2 ->
                isOk = insertIntoFile(
                        searchDir = workCurrentVersionDir,
                        fileName = it1.fileName,
                        searchText = it2.search,
                        pasteText = it2.insert,
                        pasteType = when(it2.fixType){
                            "insert" -> InsertType.INSERT
                            "reload" -> InsertType.RELOAD
                            else -> InsertType.INSERT
                        })
            }
        }

        return isOk
    }

    /**Собираем проект maven каталог проекта workCurrentVersionDir*/
    private fun runPackage(workCurrentVersionDir: String,propFile: PropFile, currentFile:Triple<String,File,String>) {
        val command = "${buildPath(propFile.mavenDir, "mvn.cmd")} package"

        Message.debug(command)
        exec(cmd = command, captureOutput = true, workingDir = File(workCurrentVersionDir))?.let {
            it.takeIf { it == ResaltState.OK }?.let {
                Message.sucsess("---------------------------------------------------------------------------")
                Message.sucsess("Проект ${currentFile.first} собран успешно")
                Message.sucsess("---------------------------------------------------------------------------")
            }
            it.takeIf { it == ResaltState.ERROR }?.let {
                Message.error("---------------------------------------------------------------------------")
                Message.error("Ошибка сборки проекта")
                Message.error("---------------------------------------------------------------------------")
                File(buildPath(propFile.workDir,currentFile.first)).renameTo(File(buildPath(propFile.workDir,"${currentFile.first}_BUILD_ERROR")))
            }
        }
    }

    private fun makePomFile(workCurrentVersionDir: String, depString: String) {
        val fileStr = File(buildPath(workCurrentVersionDir, "pom.xml")).readText()
        val newPomFile = fileStr
            .plus("<dependencies>\n")
            .plus(depString)
            .plus("</dependencies>\n")
            .plus("</project>")

        File(buildPath(workCurrentVersionDir, "pom.xml")).writeText(newPomFile)

    }

    /**Удалить файлы с расширением class*/
    inline private fun deleteClassFile(workCurrentVersionDir: String, resourceDir: String) {
        File(buildPath(workCurrentVersionDir, resourceDir))
            .walk(FileWalkDirection.TOP_DOWN)
            .filter { it.extension == "class" }
            .forEach { file ->
                file.delete().takeUnless { it }?.let { Message.error("Не удалось удалить файл ${file.name} ") }
            }
    }

    /**Ищем файл проекта*/
    private fun searchTargetFile(currentFile:Triple<String,File,String>
                                 ,propFile: PropFile
                                 ,onSearsh: (String,Triple<String,File,String>,PropFile) -> Unit): Boolean {
        val fullTargetFilePath = buildPath(propFile.workDir,currentFile.first,"Client","JAPP",propFile.projectName)
        return File(fullTargetFilePath).exists().also {
                Message.debug(fullTargetFilePath)
                if(it) {
                    Message.debug("${propFile.projectName} - найден")
                    onSearsh(fullTargetFilePath,currentFile,propFile)
                }
                else{
                    Message.debug("${propFile.projectName} - не найден")
                    File(buildPath(propFile.workDir,currentFile.first)).renameTo(File(buildPath(propFile.workDir,"${currentFile.first}_noFile")))
                    Message.debug("${buildPath(propFile.workDir,currentFile.first)} - переименован")
                }
        }
    }

    /**Проверка ниличия архиватора 7z.exe*/
    private fun checkArch(): Boolean {
        return File("7z.exe").exists().also {
            it.takeUnless { it }?.let { Message.error("$_NOT_ARJ")}
        }
    }

    /**проверяем наличие доп. файла FRREPRunner.java*/
    inline private fun checkFRREPruner(propFile: PropFile): Boolean {
        Message.info("$_CHECK_ADD_FILE - FRREPRunner.java")
        File(propFile.sourceDir?.let { buildPath(it, "FRREPRunner.java") }).exists()
            .takeUnless { it }?.let {
                Message.error("$_NOT_ADD_FILE - FRREPRunner.java")
                return false }
        return true
    }

    /**проверяем наличие доп. файла ApFileRunnerFr3.java*/
    inline private fun checkApFileRunnerFr3(propFile: PropFile): Boolean {
        Message.info("$_CHECK_ADD_FILE - ApFileRunnerFr3.java")
        File(propFile.sourceDir?.let { buildPath(it, "ApFileRunnerFr3.java") }).exists()
            .takeUnless { it }?.let {
                Message.error("$_NOT_ADD_FILE - ApFileRunnerFr3.java")
                return false }
        return true
    }

    /**Разархивирование архива */
    private fun unArchProject(propFile: PropFile, kernelFilesPair: Triple<String,File,String>): Boolean {
        val currentFile = kernelFilesPair.second
        val projectVersion = kernelFilesPair.first
        val projectVersionRenamed = kernelFilesPair.first + "_noFile"

        (File(buildPath(propFile.workDir,projectVersion)).exists() || File(buildPath(propFile.workDir,projectVersionRenamed)).exists())
            .takeIf { it }?.let {
            Message.debug("Проект версии $projectVersion существует. Все равно обработать?(y/n)")
            if(Scanner(System.`in`).next().also { Message.debug(it) }.toLowerCase() == "n") {
                return false
            }
        }

        unArchFile(buildPath(propFile.readDir!!,currentFile.name),buildPath(propFile.workDir,projectVersion))
            ?.let { it->
            it.takeUnless { it == ResaltState.OK }?.let{
                Message.error("$_ERROR_ARJ $it")
                return false
            }
        }
        Message.info("$_SUCSESS_ARJ")
        createLibrarry(propFile,kernelFilesPair,projectVersion)
        return true
    }

    /**Обновляем библиотеку каждый раз не зависимо от наличия бикомпа*/
    private fun createLibrarry(propFile: PropFile, currentFile: Triple<String,File,String>,projectVersion: String) {
        val kernelVersion = currentFile.first.substring(0,3).plus(LIBRARY_DIR_NAME)
        val fullTargetFilePath = buildPath(propFile.workDir,currentFile.first,"Client","JAPP","JInvFore.jar")
        val fullDistFilePath = buildPath(propFile.sourceDir,kernelVersion,"JInvFore.jar")
        val fullDistCorePath = buildPath(propFile.sourceDir,kernelVersion,"JInvFore_source")
        val fullTargetLibDirPath = buildPath(propFile.workDir,currentFile.first,"Client","JAPP","LIB")
        val fullDistLibDirPath = buildPath(propFile.sourceDir,kernelVersion,"LIB")


            File(fullTargetFilePath).copyOnExist(File(fullDistFilePath))?.also {
                Message.debug("Копирование $fullTargetFilePath в $fullDistFilePath")
            }
            File(fullTargetLibDirPath).copyOnExist(File(fullDistLibDirPath))?.also {
                Message.debug("Копирование $fullTargetLibDirPath в $fullDistLibDirPath")
            }
        /**нужно распаковать JInvFore для забора зависимостей*/
        unArchFile(fullDistFilePath,fullDistCorePath)
            ?.takeUnless { it == ResaltState.OK }
                ?.let{   Message.error("$_ERROR_ARJ $it")}
        /**В распакованом ядре найти нужные мне зависимости*/
        JInvFore.setDependecesFromFile(File(buildPath(fullDistCorePath,"META-INF","MANIFEST.MF")))
        JInvFore.checkDependences(buildPath(propFile.sourceDir,kernelVersion))
    }

    /**Разархивирование произвольного файла*/
    private fun unArchFile(sourceFile: String, destinationFile: String): ResaltState {
        val command = "7z.exe x -y -r $sourceFile \"-o$destinationFile\""
        Message.debug(command)
        Message.debug("Wait........")
        return exec(cmd = command,captureOutput = true)
    }

    /**Поиск архива с проектом*/
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

    /**Проверка валидности файла свойств*/
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




