package ru.shirnin.askexchange.business

import ru.shirnin.askexchange.business.general.question.prepareResult
import ru.shirnin.askexchange.inner.models.InnerQuestionContext
import ru.shirnin.askexchange.inner.models.InnerCommand
import ru.shirnin.askexchange.business.models.question.operation
import ru.shirnin.askexchange.business.models.question.stubs
import ru.shirnin.askexchange.business.permissions.question.accessValidation
import ru.shirnin.askexchange.business.permissions.question.chainPermissions
import ru.shirnin.askexchange.business.permissions.question.frontPermissions
import ru.shirnin.askexchange.business.repo.question.initRepo
import ru.shirnin.askexchange.business.repo.question.*
import ru.shirnin.askexchange.business.validation.question.*
import ru.shirnin.askexchange.business.workers.question.*
import ru.shirnin.askexchange.chain.dsl.chain
import ru.shirnin.askexchange.chain.dsl.rootChain
import ru.shirnin.askexchange.chain.dsl.worker
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerState

class InnerQuestionProcessor {

    suspend fun exec(ctx: InnerQuestionContext) = BusinessChain.execute(ctx)

    companion object {
        private val BusinessChain = rootChain {
            initStatus("Status initialization")
            initRepo("Repo initialization")

            //CREATE
            operation("Question creation", InnerCommand.CREATE) {
                stubs("Processing stubs") {
                    stubCreateSuccess("Imitating processing success")
                    stubValidationBadTitle("Imitating title validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                    stubDbError("Imitating DB error")
                }
                validation {
                    worker("Copying request") { questionValidating = questionRequest.deepCopy() }
                    worker("Cleaning title") { questionValidating.title = questionValidating.title.trim() }
                    worker("Cleaning body") { questionValidating.body = questionValidating.body.trim() }
                    validateTitleNotEmpty("Checking whether title is empty")
                    validateBodyNotEmpty("Checking whether body is empty")

                    finishQuestionValidation("Rounding up")
                }
                chainPermissions("Setting up user permissions")
                chain {
                    title = "Persistence logic"
                    repoPrepareCreate("Preparing object for saving")
                    accessValidation("Resolving access permissions")
                    repoCreate("Creating a Question in a DB")
                }
                frontPermissions("Resolving permissions for frontend")
                prepareResult("Preparing a reply")
            }

            //READ
            operation("Get a question", InnerCommand.READ) {
                stubs("Processing stubs") {
                    stubReadSuccess("Imitating processing success")
                    stubValidationBadId("Imitating id validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { questionValidating = questionRequest.deepCopy() }
                    worker("Cleaning title") {
                        questionValidating.id = InnerId(questionValidating.id.asString().trim())
                    }
                    validateIdNotEmpty("Checking whether id is empty")

                    finishQuestionValidation("Rounding up")
                }
                chainPermissions("Setting up user permissions")
                chain {
                    title = "Reading logic"
                    repoRead("Reading Question from DB")
                    accessValidation("Resolving access permissions")
                    worker {
                        title = "Preparing answer for Read"
                        isContextHealthy { state == InnerState.RUNNING }
                        handle { questionRepoDone = questionFetchedFromRepo }
                    }
                }
                frontPermissions("Resolving permissions for frontend")
                prepareResult("Preparing a reply")
            }

            //UPDATE
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
                    validateLockNotEmpty("Check if lock isn't empty")
                    validateLockProperFormat("Checking lock format")

                    finishQuestionValidation("Rounding up")
                }
                chainPermissions("Setting up user permissions")
                chain {
                    title = "Update logic"
                    repoRead("Reading Question from DB")
                    accessValidation("Resolving access permissions")
                    repoPrepareUpdate("Preparing object for an Update")
                    repoUpdate("Updating object in DB")
                }
                frontPermissions("Resolving permissions for frontend")
                prepareResult("Preparing a reply")
            }

            //DELETE
            operation("Delete a question", InnerCommand.DELETE) {
                stubs("Processing stubs") {
                    stubDeleteSuccess("Imitating processing success")
                    stubValidationBadId("Imitating id validation error")
                    stubNoCase("Error: this stub is not allowed. Check your privilege")
                }
                validation {
                    worker("Copying request") { questionValidating = questionRequest.deepCopy() }
                    worker("Cleaning title") {
                        questionValidating.id = InnerId(questionValidating.id.asString().trim())
                    }
                    validateIdNotEmpty("Checking whether id is empty")
                    validateLockNotEmpty("Check if lock isn't empty")
                    validateLockProperFormat("Checking lock format")

                    finishQuestionValidation("Rounding up")
                }
                chainPermissions("Setting up user permissions")
                chain {
                    title = "Deletion logic"
                    repoRead("Reading Question from DB")
                    accessValidation("Resolving access permissions")
                    repoPrepareDelete("Preparing object for Delete")
                    repoDelete("Deleting object from DB")
                }
                frontPermissions("Resolving permissions for frontend")
                prepareResult("Preparing a reply")
            }
        }.build()
    }
}