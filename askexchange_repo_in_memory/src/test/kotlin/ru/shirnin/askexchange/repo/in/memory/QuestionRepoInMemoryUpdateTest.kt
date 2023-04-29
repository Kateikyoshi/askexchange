package ru.shirnin.askexchange.repo.`in`.memory

import io.kotest.core.spec.style.FunSpec
import ru.shirnin.askexchange.repo.tests.BaseUpdate
import ru.shirnin.askexchange.repo.tests.updateSuccess

class QuestionRepoInMemoryUpdateTest : FunSpec({
    include(
        updateSuccess(
            QuestionRepoInMemory(
                initObjects = BaseUpdate.initObjects
            )
        )
    )
})