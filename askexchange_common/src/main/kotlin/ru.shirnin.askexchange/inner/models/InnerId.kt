package ru.shirnin.askexchange.inner.models

import kotlin.jvm.JvmInline

@JvmInline
value class InnerId(private val id: String) {
    fun asString() = id

    override fun toString(): String {
        throw NotImplementedError("Please use asString() unless you want me to find where you live and...")
    }

    companion object {
        val NONE = InnerId("")
    }
}