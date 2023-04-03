package ru.shirnin.askexchange.chain

/**
 * A block of code executing context.
 * @property title chain's title
 * @property description chain's description
 */
interface Command<T> {
    val title: String
    val description: String
    suspend fun execute(context: T)
}
