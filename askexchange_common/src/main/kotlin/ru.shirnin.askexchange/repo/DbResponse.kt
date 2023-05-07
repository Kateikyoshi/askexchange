package ru.shirnin.askexchange.repo

import ru.shirnin.askexchange.inner.models.InnerError

interface DbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<InnerError>
}