package com.dbtechprojects.dailywrestlequiz.data.di

interface ArgPersistence<T> {
    fun get(argName: String): T?
}

class StubArgPersistence<T>(private val value: Int) : ArgPersistence<Int> {

    override fun get(argName: String): Int? {
        return value
    }
}