package ru.xdweloper.bicompPreparer.fileWorkers

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

/**
 *@created 26 Август 2020 - 14:42
 *@project bicompPrepare
 *@author XDWeloper
 */

data class Changes(
    @JacksonXmlElementWrapper(useWrapping = false)
    val file: List<File> = arrayListOf()
)

data class File(
    @JacksonXmlElementWrapper(useWrapping = false)
    val modification: List<Modification> = arrayListOf() ,
    val fileName: String = ""
)

data class Modification(
    val fixType: String = "",
    val search: String = "",
    val insert: String = ""
)