package ru.shirnin.askexchange.app

import ru.shirnin.askexchange.business.InnerAnswerProcessor
import ru.shirnin.askexchange.business.InnerQuestionProcessor

data class AskAppSettings(
    val appUrls: List<String>,
    val chainSettings: AskAppChainSettings,
    val questionProcessor: InnerQuestionProcessor,
    val answerProcessor: InnerAnswerProcessor
)
