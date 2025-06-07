package com.hieunt.base.base

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hieunt.base.ui_state.ErrorsFlow
import com.hieunt.base.ui_state.LoadingState
import com.hieunt.base.ui_state.UiStateStore
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseViewModel<S : Any>: ViewModel() {
    abstract fun initState(): S

    val uiStore by lazy { UiStateStore(this.initState()) }

    val errorsFlow by lazy { ErrorsFlow() }

    val loadingState by lazy { LoadingState() }

    val currentState: S get() = uiStore.uiState

    protected val exceptionHandler by lazy { CoroutineExceptionHandler { _, exception ->
        Log.e("CoroutineExceptionHandler1901", "${this::class.java.name}: ${exception.message}")
    } }

    protected fun dispatchStateUi(uiState: S) {
        uiStore.dispatchStateUi(uiState = uiState)
    }

    protected fun updateStateUi(uiState: S) {
        uiStore.updateStateUi(uiState = uiState)
    }

    protected fun dispatchError(error: Throwable) {
        errorsFlow.emitError(error)
    }

    protected fun dispatchStateLoading(isShowLoading: Boolean){
        loadingState.updateLoadingState(isShowLoading)
    }
}