package ru.shirnin.askexchange.business

import ru.shirnin.askexchange.business.general.answer.prepareResult
import ru.shirnin.askexchange.business.models.answer.operation
import ru.shirnin.askexchange.business.models.answer.stubs
import ru.shirnin.askexchange.business.repo.answer.initRepo
import ru.shirnin.askexchange.business.repo.answer.repoCreate
import ru.shirnin.askexchange.business.repo.answer.repoPrepareCreate
import ru.shirnin.askexchange.business.repo.answer.repoRead
import ru.shirnin.askexchange.business.repo.answer.repoPrepareUpdate
import ru.shirnin.askexchange.business.repo.answer.repoUpdate
import ru.shirnin.askexchange.business.repo.answer.repoDelete
import ru.shirnin.askexchange.business.repo.answer.repoPrepareDelete
import ru.shirnin.askexchange.business.validation.answer.*
import ru.shirnin.askexchange.inner.models.InnerAnswerContext
import ru.shirnin.askexchange.inner.models.InnerCommand
import ru.shirnin.askexchange.business.workers.answer.*
import ru.shirnin.askexchange.chain.dsl.chain
import ru.shirnin.askexchange.chain.dsl.rootChain
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerState

class InnerAnswerProcessor {

    suspend fun exec(ctx: InnerAnswerContext) = BusinessChain.execute(ctx)

    companion object {
        private val BusinessChain = rootChain {
            initStatus("Status initialization")
            initRepo("Repo initialization")

            //CREATE
            operation("Answer creation", InnerCommand.CREATE) {
                stubs("Processing stubs") {
                    stubCreateSuccess("Imitating processing success")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { answerValidating = answerRequest.deepCopy() }
                    worker("Cleaning body") { answerValidating.body = answerValidating.body.trim() }
                    validateBodyNotEmpty("Checking whether body is empty")

                    finishAnswerValidation("Rounding up")
                }
                chain {
                    title = "Persistence logic"
                    repoPrepareCreate("Preparing object for saving")
                    repoCreate("Creating a Answer in a DB")
                }
                prepareResult("Preparing a reply")
            }

            //READ
            operation("Get a answer", InnerCommand.READ) {
                stubs("Processing stubs") {
                    stubReadSuccess("Imitating processing success")
                    stubValidationBadId("Imitating id validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { answerValidating = answerRequest.deepCopy() }
                    worker("Cleaning title") { answerValidating.id = InnerId(answerValidating.id.asString().trim()) }
                    validateIdNotEmpty("Checking whether id is empty")

                    finishAnswerValidation("Rounding up")
                }
                chain {
                    title = "Reading logic"
                    repoRead("Reading Answer from DB")
                    worker {
                        title = "Preparing answer for Read"
                        isContextHealthy { state == InnerState.RUNNING }
                        handle { answerRepoDone = answerFetchedFromRepo }
                    }
                }
                prepareResult("Preparing a reply")
            }

            //UPDATE
            operation("Change a answer", InnerCommand.UPDATE) {
                stubs("Processing stubs") {
                    stubUpdateSuccess("Imitating processing success")
                    stubValidationBadId("Imitating id validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { answerValidating = answerRequest.deepCopy() }
                    worker("Cleaning body") { answerValidating.body = answerValidating.body.trim() }
                    validateBodyNotEmpty("Checking whether body is empty")
                    validateLockNotEmpty("Check if lock isn't empty")
                    validateLockProperFormat("Checking lock format")

                    finishAnswerValidation("Rounding up")
                }
                chain {
                    title = "Update logic"
                    repoRead("Reading Answer from DB")
                    repoPrepareUpdate("Preparing object for an Update")
                    repoUpdate("Updating object in DB")
                }
                prepareResult("Preparing a reply")
            }

            //DELETE
            operation("Delete a answer", InnerCommand.DELETE) {
                stubs("Processing stubs") {
                    stubDeleteSuccess("Imitating processing success")
                    stubValidationBadId("Imitating id validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { answerValidating = answerRequest.deepCopy() }
                    worker("Cleaning title") { answerValidating.id = InnerId(answerValidating.id.asString().trim()) }
                    validateIdNotEmpty("Checking whether id is empty")
                    validateLockNotEmpty("Check if lock isn't empty")
                    validateLockProperFormat("Checking lock format")

                    finishAnswerValidation("Rounding up")
                }
                chain {
                    title = "Deletion logic"
                    repoRead("Reading Answer from DB")
                    repoPrepareDelete("Preparing object for Delete")
                    repoDelete("Deleting object from DB")
                }
                prepareResult("Preparing a reply")
            }
        }.build()
    }
}