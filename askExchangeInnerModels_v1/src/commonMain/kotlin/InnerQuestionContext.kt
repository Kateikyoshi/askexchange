import models.*
import models.answer.InnerAnswer
import models.question.InnerQuestion
import stubs.InnerStubs

data class InnerQuestionContext (
    var command: InnerCommand = InnerCommand.NONE,
    var state: InnerState = InnerState.NONE,
    val errors: MutableList<InnerError> = mutableListOf(),

    var workMode: InnerWorkMode = InnerWorkMode.PROD,
    var stubCase: InnerStubs = InnerStubs.NONE,
    var debugId: InnerDebugId = InnerDebugId.NONE,

    //var timeStart: Instant = Instant.NONE, //purpose is unknown yet
    var questionRequest: InnerQuestion = InnerQuestion(),
    var questionResponse: InnerQuestion = InnerQuestion(),

    var answersOfQuestionResponse: MutableList<InnerAnswer> = mutableListOf()
)