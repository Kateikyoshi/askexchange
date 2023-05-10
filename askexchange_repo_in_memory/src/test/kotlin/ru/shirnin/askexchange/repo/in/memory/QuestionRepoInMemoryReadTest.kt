package ru.shirnin.askexchange.repo.`in`.memory

import io.kotest.core.spec.style.FunSpec
import ru.shirnin.askexchange.repo.tests.question.BaseRead
import ru.shirnin.askexchange.repo.tests.question.readSuccess

class QuestionRepoInMemoryReadTest : FunSpec({
    include(
        readSuccess(
            QuestionRepoInMemory(
                initObjects = BaseRead.initObjects
            )
        )
    )
})