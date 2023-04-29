package ru.shirnin.askexchange.repo.`in`.memory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import ru.shirnin.askexchange.inner.models.InnerError
import ru.shirnin.askexchange.inner.models.InnerId
import ru.shirnin.askexchange.inner.models.answer.InnerAnswer
import ru.shirnin.askexchange.repo.`in`.memory.model.AnswerEntity
import ru.shirnin.askexchange.repo.answer.DbAnswerIdRequest
import ru.shirnin.askexchange.repo.answer.DbAnswerRequest
import ru.shirnin.askexchange.repo.answer.DbAnswerResponse
import ru.shirnin.askexchange.repo.answer.AnswerRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class AnswerRepoInMemory(
    initObjects: List<InnerAnswer> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() }
): AnswerRepository {

    private val cache = Cache.Builder<String, AnswerEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(answer: InnerAnswer) {
        val entity = AnswerEntity(answer)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    override suspend fun createAnswer(request: DbAnswerRequest): DbAnswerResponse {
        val key = randomUuid()
        val answer = request.answer.copy(id = InnerId(key))
        val entity = AnswerEntity(answer)

        cache.put(key, entity)

        return DbAnswerResponse(
            data = answer,
            isSuccess = true
        )
    }

    override suspend fun readAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
        val key = request.id.takeIf { it != InnerId.NONE }?.asString() ?: return resultErrorEmptyId

        val answer = cache.get(key)

        return if (answer != null) {
            DbAnswerResponse(
                data = answer.toInner(),
                isSuccess = true
            )
        } else {
            resultErrorNotFound
        }
    }

    override suspend fun updateAnswer(request: DbAnswerRequest): DbAnswerResponse {
        val key = request.answer.id.takeIf { it != InnerId.NONE }?.asString() ?: return resultErrorEmptyId
        val newAnswer = request.answer.copy()
        val entity = AnswerEntity(newAnswer)

        return when (cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.put(key, entity)
                DbAnswerResponse(
                    data = newAnswer,
                    isSuccess = true
                )
            }
        }
    }

    override suspend fun deleteAnswer(request: DbAnswerIdRequest): DbAnswerResponse {
        val key = request.id.takeIf { it != InnerId.NONE }?.asString() ?: return resultErrorEmptyId

        return when (val oldAnswer = cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.invalidate(key)
                DbAnswerResponse(
                    data = oldAnswer.toInner(),
                    isSuccess = true
                )
            }
        }
    }

    companion object {
        val resultErrorEmptyId = DbAnswerResponse(
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

        val resultErrorNotFound = DbAnswerResponse(
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
    }
}