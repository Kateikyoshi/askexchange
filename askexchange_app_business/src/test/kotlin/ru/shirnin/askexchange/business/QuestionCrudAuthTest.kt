package ru.shirnin.askexchange.business

import InnerQuestionStub
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.shirnin.askexchange.inner.models.*
import ru.shirnin.askexchange.inner.permissions.InnerPermissionClient
import ru.shirnin.askexchange.inner.permissions.InnerPrincipalModel
import ru.shirnin.askexchange.inner.permissions.InnerUserGroups
import ru.shirnin.askexchange.repo.`in`.memory.QuestionRepoInMemory

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionCrudAuthTest : FunSpec({
    val command = InnerCommand.CREATE
    val questionProcessor by lazy { InnerQuestionProcessor() }

    test("create auth test") {
        runTest {

            val usrId = InnerId("111")
            val repo = QuestionRepoInMemory()

            val context = InnerQuestionContext(
                settings = InnerChainSettings(
                    questionRepoTest = repo
                ),
                command = command,
                state = InnerState.NONE,
                workMode = InnerWorkMode.TEST,

                questionRequest = InnerQuestionStub.prepareResult {
                    permissionsClient.clear()
                    id = InnerId.NONE
                },

                principal = InnerPrincipalModel(
                    id = usrId,
                    groups = setOf(
                        InnerUserGroups.USER,
                        InnerUserGroups.TEST
                    )
                )
            )
            questionProcessor.exec(context)

            context.errors.size shouldBe 0
            context.state shouldBe InnerState.FINISHED

            context.questionResponse.id.asString().isNotBlank() shouldBe true
            context.questionResponse.permissionsClient.shouldContainAll(
                InnerPermissionClient.READ,
                InnerPermissionClient.UPDATE,
                InnerPermissionClient.DELETE
            )

        }
    }
})