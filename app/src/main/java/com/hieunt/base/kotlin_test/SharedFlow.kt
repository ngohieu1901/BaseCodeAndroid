package com.hieunt.base.kotlin_test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val scope = CoroutineScope(Dispatchers.IO)

    val sharedFlow = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND, // miss values
    )

    // Fast collector
//    sharedFlow
//        .onEach { println(">>> [fast collector] $it") }
//        .launchIn(scope)

    // Slow collector
    sharedFlow
        .onEach {
            delay(2_000)
            println(">>> [slow collector] $it")
        }
        .launchIn(scope)

    //emit
    scope.launch {
        repeat(10) {
            println("### Emitting $it")
            sharedFlow.emit(it) // when buffer is overflowed, it will suspend until the slow collector consumes the value
            println("### Emitted $it")
        }
    }

    delay(120_000)
    scope.cancel()
}