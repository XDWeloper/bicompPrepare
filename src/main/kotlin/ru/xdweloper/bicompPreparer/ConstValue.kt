package ru.xdweloper.bicompPreparer

/**
 *@created 26 Август 2020 - 11:45
 *@project bicompPrepare
 *@author XDWeloper
 */

enum class ResaltState(){OK, ERROR}



//-----readPropFiles.kt------------------------------
const val _READ_FILES = "Читаем файлы из каталога: "
const val _SEARCHED_FILE = "Найден файл: "
const val _READED_PROP_TEXT = "Прочитан текст: "
const val _FINDED_PROP_VALUE = "Найдено свойство: "
const val _NOT_PROP_VALUE = "Не заданно свойство: "



//-----startApp.kt------------------------------
const val _INPUT_TEXT = "Введеный текст: "
const val _SERACHED_PROPFILE = "Найден файл свойств "
const val _Q_SERACHED_PROPFILE = "запустить обработку?(y/n):"

//-----ChangeFileReader------------------------------
const val _SEARCH_CHANGE_FILE = "Ищем файл замен: "
const val _LADED_CHANGE_FILE = "Файл замен успешно обработан!"
const val _LADED_FILE_ERROR = "Ошибка обработки файла замен"

//-----mainProcess------------------------------------
const val _CHECK_PROP_FILE = "проверка данных в файле "
const val _CHECK_PROP_SUCSESS = "Свойства успешно проверены "
const val _CHECK_PROP_DATA_PATH = "Не найден путь "
const val _CHECK_PROP_DATA_FILE = "Не найден файл "
const val _CHECK_ADD_FILE = "Проверяем наличие доп.файлов "
const val _NOT_ADD_FILE = "Отсутствуют доп.файлы "
const val _FIND_PROJECT_FILE = "Поиск архива с проектом "
const val _NOT_PROJECT_FILE = "Файлы удовлетворяющие условию KERNEL отсутствуют "
const val _NOT_ARJ = "Не найден штатный архиватор 7z.exe скопируйте архиватор в папку с пректом"
const val _ERROR_ARJ = "Ошибка архиватора"
const val _SUCSESS_ARJ = "Проект успешно разархивирован"



