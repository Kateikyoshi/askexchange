import InnerAnswerStubKotlin.ANSWER_KOTLIN
import models.answer.InnerAnswer

object InnerAnswerStub {
    fun get(): InnerAnswer = ANSWER_KOTLIN.copy()
}