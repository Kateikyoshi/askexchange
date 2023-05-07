package ru.shirnin.askexchange.repo.`in`.memory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.InnerVersionLock
import ru.shirnin.askexchange.inner.models.helpers.errorRepoConcurrency
import ru.shirnin.askexchange.inner.models.question.InnerQuestion
import ru.shirnin.askexchange.repo.`in`.memory.model.QuestionEntity
import ru.shirnin.askexchange.repo.question.DbQuestionIdRequest
import ru.shirnin.askexchange.repo.question.DbQuestionRequest
import ru.shirnin.askexchange.repo.question.DbQuestionResponse
import ru.shirnin.askexchange.repo.question.QuestionRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class QuestionRepoInMemory(
    initObjects: List<InnerQuestion> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() }
) : QuestionRepository {

    private val mutex: Mutex = Mutex()

    private val cache = Cache.Builder<String, QuestionEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(question: InnerQuestion) {
        val entity = QuestionEntity(question)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createQuestion(request: DbQuestionRequest): DbQuestionResponse {
        val key = randomUuid()
        val question = request.question.copy(id = InnerId(key))
        val entity = QuestionEntity(question)

        cache.put(key, entity)

        return DbQuestionResponse(
            data = question,
            isSuccess = true
        )
    }

    override suspend fun readQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
        val key = request.id.takeIf { it != InnerId.NONE }?.asString() ?: return resultErrorEmptyId

        val question = cache.get(key)

        return if (question != null) {
            DbQuestionResponse(
                data = question.toInner(),
                isSuccess = true
            )
        } else {
            resultErrorNotFound
        }
    }

    override suspend fun updateQuestion(request: DbQuestionRequest): DbQuestionResponse {
        val key = request.question.id.takeIf { it != InnerId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldVersionLock =
            request.question.lock.takeIf { it != InnerVersionLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val newQuestion = request.question.copy()
        val entity = QuestionEntity(newQuestion)

        return checkLockAndUpdate(key, oldVersionLock) {
            cache.put(key, entity)
            DbQuestionResponse(
                data = newQuestion,
                isSuccess = true
            )
        }
    }


    override suspend fun deleteQuestion(request: DbQuestionIdRequest): DbQuestionResponse {
        val key = request.id.takeIf { it != InnerId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldVersionLock =
            request.lock.takeIf { it != InnerVersionLock.NONE }?.asString() ?: return resultErrorEmptyLock

        return checkLockAndUpdate(key, oldVersionLock) { oldQuestion ->
            cache.invalidate(key)
            DbQuestionResponse(
                data = oldQuestion.toInner(),
                isSuccess = true
            )
        }
    }


    private suspend fun checkLockAndUpdate(
        key: String,
        oldVersionLock: String,
        codePiece: (oldQuestion: QuestionEntity) -> DbQuestionResponse
    ): DbQuestionResponse = mutex.withLock {
        val oldQuestion = cache.get(key)
        when {
            oldQuestion == null -> resultErrorNotFound
            oldQuestion.lock != oldVersionLock -> DbQuestionResponse(
                data = oldQuestion.toInner(),
                isSuccess = false,
                errors = listOf(
                    errorRepoConcurrency(
                        InnerVersionLock(oldVersionLock),
                        oldQuestion.lock?.let { InnerVersionLock(it) }
                    )
                )
            )

            else -> codePiece(oldQuestion)
        }
    }

    companion object {
        val resultErrorEmptyId = DbQuestionResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                InnerError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )

        val resultErrorNotFound = DbQuestionResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                InnerError(
                    code = "not-found",
                    field = "id",
                    message = "Not found"
                )
            )
        )

        val resultErrorEmptyLock = DbQuestionResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                InnerError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank"
                )
            )
        )
    }
}