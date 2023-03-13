import InnerQuestionStubKotlin.QUESTION_KOTLIN
import models.question.InnerQuestion

object InnerQuestionStub {
    fun get(): InnerQuestion = QUESTION_KOTLIN.copy()
}