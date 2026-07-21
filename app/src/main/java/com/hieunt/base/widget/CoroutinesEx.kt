package com.hieunt.base.widget

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.contracts.ExperimentalContracts
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

private const val STOP_TIMEOUT_MILLIS: Long = 5000
val WHILE_UI_SUBSCRIBED: SharingStarted = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS)

@OptIn(ExperimentalContracts::class)
suspend inline fun <R> runSuspendCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> R,
): Result<R> {
    return try {
        Result.success(withContext(context) { block() })
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

fun Fragment.launchAndRepeatWhenViewStarted(
    launchBlock: suspend () -> Unit,
    vararg launchBlocks: suspend () -> Unit,
): Job =
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            launch { launchBlock() }
            launchBlocks.forEach { launch { it() } }
        }
    }

fun LifecycleOwner.launchAndRepeatWhenStarted(
    launchBlock: suspend () -> Unit,
    vararg launchBlocks: suspend () -> Unit,
): Job =
    lifecycleScope.launch {
        repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            launch { launchBlock() }
            launchBlocks.forEach { launch { it() } }
        }
    }