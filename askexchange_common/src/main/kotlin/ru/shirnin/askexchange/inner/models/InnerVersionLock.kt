package ru.shirnin.askexchange.inner.models

@JvmInline
value class InnerVersionLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = InnerVersionLock("")
    }
}