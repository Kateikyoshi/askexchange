package ru.shirnin.askexchange.chain

data class SomeContext(
    var status: CommandStatuses = CommandStatuses.NONE,
    var some: Int = 0,
    var history: String = "",
)

enum class CommandStatuses {
    NONE,
    RUNNING,
    FAILING,
    ERROR
}