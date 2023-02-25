package models

import kotlin.jvm.JvmInline

@JvmInline
value class InnerId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = InnerId("")
    }
}