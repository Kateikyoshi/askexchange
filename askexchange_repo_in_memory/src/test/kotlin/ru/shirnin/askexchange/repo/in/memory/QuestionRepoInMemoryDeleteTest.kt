package ru.shirnin.askexchange.repo.`in`.memory

import io.kotest.core.spec.style.FunSpec
import ru.shirnin.askexchange.repo.tests.question.BaseDelete
import ru.shirnin.askexchange.repo.tests.question.deleteSuccess

class QuestionRepoInMemoryDeleteTest : FunSpec({
    include(
        deleteSuccess(
            QuestionRepoInMemory(
                initObjects = BaseDelete.initObjects
            )
        )
    )
})