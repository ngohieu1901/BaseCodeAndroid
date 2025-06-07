package com.hieunt.base.state

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ErrorsState {
    private val _errors = MutableSharedFlow<Throwable>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val errorsFlow: SharedFlow<Throwable> get() = _errors.asSharedFlow()

    fun emitError(t: Throwable) {
        _errors.tryEmit(t)
    }
}