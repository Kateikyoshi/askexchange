import InnerAnswerStubKotlin.ANSWER_KOTLIN
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer

object InnerAnswerStub {
    fun get(): InnerAnswer = ANSWER_KOTLIN.copy()

    fun prepareResult(block: InnerAnswer.() -> Unit): InnerAnswer = get().apply(block)
}