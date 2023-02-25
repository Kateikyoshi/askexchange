package models

import kotlin.jvm.JvmInline

@JvmInline
value class InnerDebugId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = InnerDebugId("")
    }
}