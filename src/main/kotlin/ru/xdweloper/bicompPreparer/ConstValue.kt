package ru.xdweloper.bicompPreparer

/**
 *@created 26 Август 2020 - 11:45
 *@project bicompPrepare
 *@author XDWeloper
 */

enum class ReaderState(){OK, ERROR}


//-----readPropFiles.kt------------------------------
const val _READ_FILES = "Читаем файлы из каталога: "
const val _SEARCHED_FILE = "Найден файл: "
const val _READED_PROP_TEXT = "Прочитан текст: "
const val _FINDED_PROP_VALUE = "Найдено свойство: "


//-----startApp.kt------------------------------
const val _INPUT_TEXT = "Введеный текст: "

//-----ChangeFileReader------------------------------
const val _SEARCH_CHANGE_FILE = "Ищем файл замен: "
const val _LADED_CHANGE_FILE = "Файл замен успешно обработан!"
const val _LADED_FILE_ERROR = "Ошибка обработки файла замен"