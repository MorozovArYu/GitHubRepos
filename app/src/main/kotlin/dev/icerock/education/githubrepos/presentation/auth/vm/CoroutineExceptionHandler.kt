package dev.icerock.education.githubrepos.presentation.auth.vm

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class ErrorHandler() : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*>
        get() = TODO("Not yet implemented")

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        TODO("Not yet implemented")
    }
}