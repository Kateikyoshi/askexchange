package ru.shirnin.askexchange.business

import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerCommand
import ru.shirnin.askexchange.business.models.question.operation
import ru.shirnin.askexchange.business.models.question.stubs
import ru.shirnin.askexchange.business.validation.question.*
import ru.shirnin.askexchange.business.workers.question.*
import ru.shirnin.askexchange.chain.dsl.rootChain
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerId

class InnerQuestionProcessor {
    suspend fun exec(ctx: InnerQuestionContext) = BusinessChain.execute(ctx)

    companion object {
        private val BusinessChain = rootChain {
            initStatus("Status initialization")

            operation("Question creation", InnerCommand.CREATE) {
                stubs("Processing stubs") {
                    stubCreateSuccess("Imitating processing success")
                    stubValidationBadTitle("Imitating title validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { questionValidating = questionRequest.deepCopy() }
                    worker("Cleaning title") { questionValidating.title = questionValidating.title.trim() }
                    worker("Cleaning body") { questionValidating.body = questionValidating.body.trim() }
                    validateTitleNotEmpty("Checking whether title is empty")
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
                    worker("Copying request") { questionValidating = questionRequest.deepCopy() }
                    worker("Cleaning title") { questionValidating.id = InnerId(questionValidating.id.toString().trim()) }
                    validateIdNotEmpty("Checking whether id is empty")

                    finishQuestionValidation("Rounding up")
                }
            }
            operation("Change a question", InnerCommand.UPDATE) {
                stubs("Processing stubs") {
                    stubUpdateSuccess("Imitating processing success")
                    stubValidationBadId("Imitating id validation error")
                    stubValidationBadTitle("Imitating title validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { questionValidating = questionRequest.deepCopy() }
                    worker("Cleaning title") { questionValidating.title = questionValidating.title.trim() }
                    worker("Cleaning body") { questionValidating.body = questionValidating.body.trim() }
                    validateTitleNotEmpty("Checking whether title is empty")
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
                    worker("Copying request") { questionValidating = questionRequest.deepCopy() }
                    worker("Cleaning title") { questionValidating.id = InnerId(questionValidating.id.toString().trim()) }
                    validateIdNotEmpty("Checking whether id is empty")

                    finishQuestionValidation("Rounding up")
                }
            }
        }.build()
    }
}