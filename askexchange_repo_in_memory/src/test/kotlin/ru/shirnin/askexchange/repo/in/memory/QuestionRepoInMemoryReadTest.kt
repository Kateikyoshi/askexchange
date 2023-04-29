package ru.shirnin.askexchange.repo.`in`.memory

import io.kotest.core.spec.style.FunSpec
import ru.shirnin.askexchange.repo.tests.BaseDelete
import ru.shirnin.askexchange.repo.tests.BaseRead
import ru.shirnin.askexchange.repo.tests.deleteSuccess
import ru.shirnin.askexchange.repo.tests.readSuccess

class QuestionRepoInMemoryReadTest : FunSpec({
    include(
        readSuccess(
            QuestionRepoInMemory(
                initObjects = BaseRead.initObjects
            )
        )
    )
})