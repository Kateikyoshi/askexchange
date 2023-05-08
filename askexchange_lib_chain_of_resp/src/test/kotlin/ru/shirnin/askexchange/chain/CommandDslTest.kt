package ru.shirnin.askexchange.chain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.shirnin.askexchange.chain.dsl.CommandBuilder
import ru.shirnin.askexchange.chain.dsl.rootChain
import ru.shirnin.askexchange.chain.dsl.worker

private suspend fun buildAndExecute(dsl: CommandBuilder<SomeContext>): SomeContext {
    val ctx = SomeContext()

    dsl.build().execute(ctx)

    return ctx
}

@OptIn(ExperimentalCoroutinesApi::class)
class CommandsDslTest : FunSpec({
    test("handle should execute") {
        runTest {
            buildAndExecute(rootChain {
                worker {
                    handle { history = "bob" }
                }
            }).history shouldBe "bob"
        }
    }

    test("condition check") {
        runTest {
            buildAndExecute(rootChain {
                worker {
                    isContextHealthy { status == CommandStatuses.ERROR }
                    handle { history += "w1; " }
                }
                worker {
                    isContextHealthy { status == CommandStatuses.NONE }
                    handle {
                        history += "w2; "
                        status = CommandStatuses.FAILING
                    }
                }
                worker {
                    isContextHealthy { status == CommandStatuses.FAILING }
                    handle { history += "ww3; " }
                }
            }).history shouldBe "w2; ww3; "
        }
    }
})