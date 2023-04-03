package ru.shirnin.askexchange.app.v1

import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.business.InnerAnswerProcessor
import ru.shirnin.askexchange.business.InnerQuestionProcessor


private val questionProcessor = InnerQuestionProcessor()
suspend fun process(ctx: InnerQuestionContext) = questionProcessor.exec(ctx)



private val answerProcessor = InnerAnswerProcessor()
suspend fun process(ctx: InnerAnswerContext) = answerProcessor.exec(ctx)
