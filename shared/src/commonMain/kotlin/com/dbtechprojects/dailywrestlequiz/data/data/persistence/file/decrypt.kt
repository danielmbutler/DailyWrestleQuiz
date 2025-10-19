package com.dbtechprojects.dailywrestlequiz.data.data.persistence.file



expect fun decryptFileToJson(fileName: String, key: String): String?

expect fun getKey(): String
