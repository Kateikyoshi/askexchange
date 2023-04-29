package ru.shirnin.askexchange.business

import ru.shirnin.askexchange.business.models.answer.operation
import ru.shirnin.askexchange.business.models.answer.stubs
import ru.shirnin.askexchange.business.validation.answer.finishQuestionValidation
import ru.shirnin.askexchange.business.validation.answer.validateBodyNotEmpty
import ru.shirnin.askexchange.business.validation.answer.validateIdNotEmpty
import ru.shirnin.askexchange.business.validation.answer.validation
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerCommand
import ru.shirnin.askexchange.business.workers.answer.*
import ru.shirnin.askexchange.chain.dsl.rootChain
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerId

class InnerAnswerProcessor {

    suspend fun exec(ctx: InnerAnswerContext) = BusinessChain.execute(ctx)

    companion object {
        private val BusinessChain = rootChain{
            initStatus("Status initialization")

            operation("Question creation", InnerCommand.CREATE) {
                stubs("Processing stubs") {
                    stubCreateSuccess("Imitating processing success")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { answerValidating = answerRequest.deepCopy() }
                    worker("Cleaning body") { answerValidating.body = answerValidating.body.trim() }
                    validateBodyNotEmpty("Checking whether body is empty")

                    finishQuestionValidation("Rounding up")
                }
            }
            operation("Get a question", InnerCommand.READ) {
                stubs("Processing stubs") {
                    stubReadSuccess("Imitating processing success")
                    stubValidationBadId("Imitating id validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { answerValidating = answerRequest.deepCopy() }
                    worker("Cleaning title") { answerValidating.id = InnerId(answerValidating.id.toString().trim()) }
                    validateIdNotEmpty("Checking whether id is empty")

                    finishQuestionValidation("Rounding up")
                }
            }
            operation("Change a question", InnerCommand.UPDATE) {
                stubs("Processing stubs") {
                    stubUpdateSuccess("Imitating processing success")
                    stubValidationBadId("Imitating id validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { answerValidating = answerRequest.deepCopy() }
                    worker("Cleaning body") { answerValidating.body = answerValidating.body.trim() }
                    validateBodyNotEmpty("Checking whether body is empty")

                    finishQuestionValidation("Rounding up")
                }
            }
            operation("Delete a question", InnerCommand.DELETE) {
                stubs("Processing stubs") {
                    stubDeleteSuccess("Imitating processing success")
                    stubValidationBadId("Imitating id validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { answerValidating = answerRequest.deepCopy() }
                    worker("Cleaning title") { answerValidating.id = InnerId(answerValidating.id.toString().trim()) }
                    validateIdNotEmpty("Checking whether id is empty")

                    finishQuestionValidation("Rounding up")
                }
            }
        }.build()
    }
}