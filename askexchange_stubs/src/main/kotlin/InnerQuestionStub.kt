import InnerQuestionStubKotlin.QUESTION_KOTLIN
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

object InnerQuestionStub {
    fun get(): InnerQuestion = QUESTION_KOTLIN.copy()

    fun prepareResult(block: InnerQuestion.() -> Unit): InnerQuestion = get().apply(block)
}