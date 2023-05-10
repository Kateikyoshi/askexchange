package ru.shirnin.askexchange.repo.`in`.memory

import io.kotest.core.spec.style.FunSpec
import ru.shirnin.askexchange.repo.tests.question.BaseUpdate
import ru.shirnin.askexchange.repo.tests.question.updateSuccess

class QuestionRepoInMemoryUpdateTest : FunSpec({
    include(
        updateSuccess(
            QuestionRepoInMemory(
                initObjects = BaseUpdate.initObjects
            )
        )
    )
})