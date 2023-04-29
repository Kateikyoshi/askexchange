package ru.shirnin.askexchange.repo.`in`.memory

import io.kotest.core.spec.style.FunSpec
import ru.shirnin.askexchange.repo.tests.BaseDelete
import ru.shirnin.askexchange.repo.tests.deleteSuccess

class QuestionRepoInMemoryDeleteTest : FunSpec({
    include(
        deleteSuccess(
            QuestionRepoInMemory(
                initObjects = BaseDelete.initObjects
            )
        )
    )
})