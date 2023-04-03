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

//
//    @Test
//    @JsName("on_should_check_condition")
//    fun `on should check condition`() = runTest {
//        assertEquals("w2; w3; ", execute(rootChain {
//            worker {
//                on { status == CorStatuses.ERROR }
//                handle { history += "w1; " }
//            }
//            worker {
//                on { status == CorStatuses.NONE }
//                handle {
//                    history += "w2; "
//                    status = CorStatuses.FAILING
//                }
//            }
//            worker {
//                on { status == CorStatuses.FAILING }
//                handle { history += "w3; " }
//            }
//        }).history)
//    }
//
//    @Test
//    @JsName("except_should_execute_when_exception")
//    fun `except should execute when exception`() = runTest {
//        assertEquals("some error", execute(rootChain {
//            worker {
//                handle { throw RuntimeException("some error") }
//                except { history += it.message }
//            }
//        }).history)
//    }
//
//    @Test
//    @JsName("should_throw_when_exception_and_no_except")
//    fun `should throw when exception and no except`() = runTest {
//        assertFails {
//            execute(rootChain {
//                worker("throw") { throw RuntimeException("some error") }
//            })
//        }
//    }
//
//    @Test
//    @JsName("complex_chain_example")
//    fun `complex chain example`() = runTest {
//        val chain = rootChain<TestContext> {
//            worker {
//                title = "Инициализация статуса"
//                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"
//
//                on { status == CorStatuses.NONE }
//                handle { status = CorStatuses.RUNNING }
//                except { status = CorStatuses.ERROR }
//            }
//
//            chain {
//                on { status == CorStatuses.RUNNING }
//
//                worker(
//                    title = "Лямбда обработчик",
//                    description = "Пример использования обработчика в виде лямбды"
//                ) {
//                    some += 4
//                }
//            }
//
//            parallel {
//                on {
//                    some < 15
//                }
//
//                worker(title = "Increment some") {
//                    some++
//                }
//            }
//
//            printResult()
//
//        }.build()
//
//        val ctx = TestContext()
//        chain.exec(ctx)
//        println("Complete: $ctx")
//    }
//}
//
//private fun ICorChainDsl<TestContext>.printResult() = worker(title = "Print example") {
//    println("some = $some")
//}