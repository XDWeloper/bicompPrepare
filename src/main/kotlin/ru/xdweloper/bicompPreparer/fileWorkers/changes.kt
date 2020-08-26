package ru.xdweloper.bicompPreparer.fileWorkers

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText


/**
 *@created 26 Август 2020 - 14:42
 *@project bicompPrepare
 *@author XDWeloper
 */

//@JsonIgnoreProperties(ignoreUnknown = true)
data class Changes(
    @JacksonXmlElementWrapper(useWrapping = false)
    val file: List<file> = arrayListOf()
)

data class file(
    @JacksonXmlText
    val file: String = ""
    ,
    @JacksonXmlProperty(isAttribute = true)
    val fileName: String = ""


)