package ru.shirnin.askexchange.repo.`in`.memory

import io.kotest.core.spec.style.FunSpec
import ru.shirnin.askexchange.repo.tests.question.BaseCreate
import ru.shirnin.askexchange.repo.tests.question.createSuccess

class QuestionRepoInMemoryCreateTest : FunSpec({

    include(
        createSuccess(
            QuestionRepoInMemory(
                initObjects = BaseCreate.initObjects
            )
        )
    )
})
