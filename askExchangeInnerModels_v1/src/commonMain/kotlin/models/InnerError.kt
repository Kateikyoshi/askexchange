package models

data class InnerError(
    val code: String = "",
    val message: String = "",
    val exception: Throwable? = null
)