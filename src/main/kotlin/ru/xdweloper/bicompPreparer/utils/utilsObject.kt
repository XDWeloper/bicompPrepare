package ru.xdweloper.bicompPreparer.utils

import com.andreapivetta.kolor.*
import com.sun.org.apache.xpath.internal.operations.Bool
import org.slf4j.LoggerFactory
import ru.xdweloper.bicompPreparer.ResaltState
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 *@created 25 Август 2020 - 16:48
 *@project bicompPrepare
 *@author XDWeloper
 */

enum class MessageType {ERROR,INFO, DEBUG,SUCSESS}
enum class InsertType {INSERT,RELOAD}

object  Message {
    fun info(message: String){
        print(message,MessageType.INFO)
    }

    fun error(message: String){
        print(message,MessageType.ERROR)
    }

    fun debug(message: String){
        print(message,MessageType.DEBUG)
    }

    fun sucsess(message: String){
        print(message,MessageType.SUCSESS)
    }


        fun print(message: String, messageType: MessageType) {
            when (messageType) {
                MessageType.INFO -> {println(message.lightGreen())
                                        getXLogger("All pojo").info(message)
                                        }
                MessageType.ERROR -> {println(message.red())
                                        getXLogger("All pojo").error(message)
                                        }
                MessageType.DEBUG -> {println(message.lightBlue())
                                        getXLogger("All pojo").debug(message)
                                        }
                MessageType.SUCSESS -> {println(message.yellow())
                    getXLogger("All pojo").debug(message)
                }
            }

        }
}

inline fun getXLogger(className: String?) = LoggerFactory.getLogger(className)

fun buildPath(vararg strList: String) = buildString{
    for((index,str) in strList.withIndex()){
        val s = if(index != (strList.size - 1))
            File.separator
        else ""
            append(str).append(s)
    }
}

fun exec(cmd: String, stdIn: String = "", captureOutput:Boolean = false, workingDir: File = File(".")): ResaltState {
    try {
        val process = ProcessBuilder(*cmd.split("\\s".toRegex()).toTypedArray())
            .directory(workingDir)
            .redirectOutput(if (captureOutput) ProcessBuilder.Redirect.PIPE else ProcessBuilder.Redirect.INHERIT)
            .redirectError(if (captureOutput) ProcessBuilder.Redirect.PIPE else ProcessBuilder.Redirect.INHERIT)
            .start().apply {
                do{
                    val line = inputStream.bufferedReader().readLine()
                    line?.let {
                        if(it.contains("Everything is Ok") || it.contains("BUILD SUCCESS")) {
                            Message.sucsess("---------------------------------------------------------------------------")
                            Message.sucsess(it)
                            Message.sucsess("---------------------------------------------------------------------------")
                            return ResaltState.OK
                        }else Message.info(it)
                    }
                }while(line != null)


                if (stdIn != "") {
                    outputStream.bufferedWriter().apply {
                        write(stdIn)
                        flush()
                        close()
                    }
                }
                waitFor(10, TimeUnit.MINUTES)
            }


//        if (captureOutput) {
//            return process.inputStream.bufferedReader().readText()
//        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ResaltState.ERROR
}

object JInvFore{

    var coreDependences: MutableList<String> = mutableListOf()

    fun setDependecesFromFile(pomFile : File){
        coreDependences.clear()
        pomFile.exists().takeIf { it }?.let {
            val fileString = pomFile.readText()
                val firstPart = fileString.substringBeforeLast("Implementation-Vendor:")
                val secondPart = firstPart.substringAfter("Class-Path:")
                    .replace("\r\n","")
                    .replace(".jar ", ".jar$")
                    .replace(" ","")
                    .replace("/",File.separator)
                coreDependences = secondPart.split("$") as MutableList<String>
            coreDependences.size.takeIf { it > 0 }?.let { Message.info("Зависимости  найдены и прочитаны") }
            coreDependences.size.takeIf { it == 0 }?.let { Message.error("Зависимости не найдены проверьте наличие файла JInvFore в каталоге источнике")}
            /**Добавить само ядро*/
            coreDependences.add("JInvFore.jar")
        }
    }

    fun getDepString(version: String = "1.0.0",groupId: String = "ru.inversion.jp", sourcePath: String) = buildString {
        coreDependences.forEach {
            val artifactId = it.replace("""\""","_").replace("-","_").replace(".","_")
            append("<dependency>" +
                    "<groupId>$groupId</groupId>" +
                    "<version>$version</version>" +
                    "<artifactId>$artifactId</artifactId>" +
                    "<scope>system</scope>" +
                    "<systemPath>${buildPath(sourcePath.replace("""\\""","""\"""),it)}</systemPath>" +
                    "</dependency>\n")
        }
    }

    fun checkDependences(sourcePath: String) {
        val tmpCore : MutableList<String> = mutableListOf()
        for(lib in coreDependences){
            if(!File(buildPath(sourcePath,lib)).exists()) {
                Message.error("файл из зависимотстей ------- $lib не найден")
            }else tmpCore.add(lib)
        }
        coreDependences.clear()
        coreDependences.addAll(tmpCore)
val i = 0
    }
}

fun insertIntoFile(searchDir: String,
                   fileName:String,
                   searchText: String,
                   pasteText:String,
                   pasteType: InsertType): Boolean {

    var clearSearchText = searchText.clearText()
    var changedFileText: String

    File(searchDir).walkTopDown()
        .filter {it1 -> it1.isFile && it1.name == fileName }
        .forEach {
            var fileText = it.readText().clearText()
            changedFileText = fileText
            if(!fileText.contains(clearSearchText)) { Message.error("Не найден текст $searchText  \n ----------------------------------- в файле $fileName") }
            else{
                Message.sucsess(" В файле $fileName найден текст:")
                Message.debug("$searchText ")
                if(pasteType == InsertType.INSERT) {
                    val firstPart = fileText.substringBefore(clearSearchText,"ERROR")
                    val secondPart = fileText.substringAfter(clearSearchText,"ERROR")
                    if(firstPart != "ERROR" && secondPart != "ERROR")
                        changedFileText = firstPart.plus(clearSearchText).plus(pasteText).plus(secondPart)
                    else{
                        Message.error("Ошибка вставки текста $searchText \n ----------------------------------- в файл $fileName")
                        changedFileText = fileText
                    }
                } else{
                    changedFileText = fileText.replace(clearSearchText,pasteText)
                }
                changedFileText = changedFileText.replace("%%%","$")
                Message.sucsess("-------------------------- Произведена вставка текста -------------------------")
                Message.info("Сохраняем файл ${it.absoluteFile} \n ------------------------------------------------------------------------ ")
                it.writeText(changedFileText)
            }
        }

    return true
}

fun String.clearText() = this
    .replace("[ ]+".toRegex()," ")
    .replace("\r\n", "\n")
    .replace("$","%%%")
    .replace("\t","")
    .trim()





