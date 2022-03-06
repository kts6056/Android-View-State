package com.kts6056.viewstate.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

abstract class BaseViewModel : ViewModel() {
    private val _jobs: MutableMap<String, Job> = mutableMapOf()
    private val defaultJobKey: String
        get() = Thread.currentThread().stackTrace.getOrNull(4)?.methodName ?: ""

    private val _errorHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }

    protected open fun handleException(exception: Throwable) {
    }

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        errorHandler: CoroutineExceptionHandler = _errorHandler,
        block: suspend CoroutineScope.() -> Unit
    ): Job = (viewModelScope + errorHandler).launch(context, start, block)

    protected fun launchLatest(
        key: String = defaultJobKey,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        errorHandler: CoroutineExceptionHandler = _errorHandler,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        _jobs[key]?.cancel()
        return launch(context, start, errorHandler, block).also {
            _jobs[key] = it
        }
    }

    protected fun jobCancel(key: String = defaultJobKey) = _jobs[key]?.cancel()

    protected fun <T> async(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ): Deferred<T> = viewModelScope.async(context, start, block)
}
