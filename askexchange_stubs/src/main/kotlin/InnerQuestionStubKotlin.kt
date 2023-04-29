import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.question.InnerQuestion

object InnerQuestionStubKotlin {
    val QUESTION_KOTLIN: InnerQuestion
        get() = InnerQuestion(
            id = InnerId("1"),
            title = "New language Kotlin",
            body = "I've heard Kotlin is some new JVM thingie. What is it about?",
            username = "Josh"
        )
}