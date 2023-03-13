import kotlinx.datetime.Instant
import models.InnerId
import models.answer.InnerAnswer

object InnerAnswerStubKotlin {
    val ANSWER_KOTLIN: InnerAnswer
        get() = InnerAnswer(
            id = InnerId("999"),
            body = "Kotlin is a programming language full syntactic sugar, not more, not less",
            date = Instant.fromEpochMilliseconds(100000),
            likes = 101
        )
}