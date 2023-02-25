import models.*
import models.answer.InnerAnswer
import models.question.InnerQuestion
import stubs.InnerStubs

data class InnerAnswerContext (
    var command: InnerCommand = InnerCommand.NONE,
    var state: InnerState = InnerState.NONE,
    val errors: MutableList<InnerError> = mutableListOf(),

    var workMode: InnerWorkMode = InnerWorkMode.PROD,
    var stubCase: InnerStubs = InnerStubs.NONE,
    var debugId: InnerDebugId = InnerDebugId.NONE,


    var user: InnerUser = InnerUser(),
    var question: InnerQuestion = InnerQuestion(),

    var answerRequest: InnerAnswer = InnerAnswer(),
    var answerResponse: InnerAnswer = InnerAnswer()
)

