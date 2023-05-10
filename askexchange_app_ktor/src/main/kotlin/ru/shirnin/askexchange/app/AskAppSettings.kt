package ru.shirnin.askexchange.app

import ru.shirnin.askexchange.business.InnerAnswerProcessor
import ru.shirnin.askexchange.business.InnerQuestionProcessor
import ru.shirnin.askexchange.inner.models.InnerChainSettings

data class AskAppSettings(
    val appUrls: List<String>,
    val chainSettings: InnerChainSettings,
    val questionProcessor: InnerQuestionProcessor,
    val answerProcessor: InnerAnswerProcessor
)
