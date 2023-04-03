package ru.shirnin.askexchange.chain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.lang.IllegalArgumentException

@OptIn(ExperimentalCoroutinesApi::class)
class CommandsTest : FunSpec({
    test("worker should execute handle") {
        runTest {
            val worker = CommandWorker<SomeContext>(
                title = "w1",
                handler = { history += "w1; " },
                contextChecker = { true }
            )
            val ctx = SomeContext()
            worker.execute(ctx)

            ctx.history shouldBe "w1; "
        }
    }

    test("worker should not execute when check fails") {
        runTest {
            val worker = CommandWorker<SomeContext>(
                title = "w1",
                contextChecker = { false }
            )
            val ctx = SomeContext(history = "gals")
            worker.execute(ctx)

            ctx.history shouldBe "gals"
        }
    }

    test("worker should handle exception") {
        runTest {
            val exceptionMessage = "wowzers"

            val worker = CommandWorker<SomeContext>(
                title = "w1",
                exceptionHandler = { e -> history = e.message ?: "" },
                handler = { throw IllegalArgumentException(exceptionMessage) },
                contextChecker = { true }
            )
            val ctx = SomeContext()
            worker.execute(ctx)

            ctx.history shouldBe exceptionMessage
        }
    }

    //chain time! -----------------------------------------------------------------------------------

    test("chain should execute workers") {
        runTest {
            val createWorker = { title: String ->
                CommandWorker<SomeContext>(
                    title = title,
                    contextChecker = { status == CommandStatuses.NONE },
                    handler = { history += title }
                )
            }

            val chain = CommandChain(
                commands = listOf(createWorker("w1"), createWorker("w2")),
                title = "chirp",
                handler = ::executeSequential
            )

            val ctx = SomeContext()

            chain.execute(ctx)

            ctx.history shouldBe "w1w2"
        }
    }
})


