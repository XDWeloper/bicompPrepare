package ru.xdweloper.bicompPreparer.utils

import java.io.File

/**
 *@created 31 Август 2020 - 12:11
 *@project bicompPrepare
 *@author XDWeloper
 */


fun File.copyOnExist(distFile: File): Boolean? =
    with(this){
        exists().takeIf { it }?.let {
            copyRecursively(distFile,true)
            return@with true
        } }
