package com.dbtechprojects.dailywrestlequiz.data.di

interface ArgPersistence<T> {
    fun get(argName: String): T?
}

class StubArgPersistence<T>(private val value: T?) : ArgPersistence<T?> {

    override fun get(argName: String): T? {
        return value
    }
}