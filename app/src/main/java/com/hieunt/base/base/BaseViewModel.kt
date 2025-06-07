package com.hieunt.base.base

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hieunt.base.state.ErrorsState
import com.hieunt.base.state.LoadingState
import com.hieunt.base.state.UiState
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseViewModel<S : Any>: ViewModel() {
    abstract fun initState(): S

    val uiState by lazy { UiState(this.initState()) }

    val errorsState by lazy { ErrorsState() }

    val loadingState by lazy { LoadingState() }

    val currentState: S get() = uiState.currentUiState

    protected val exceptionHandler by lazy { CoroutineExceptionHandler { _, exception ->
        Log.e("CoroutineExceptionHandler1901", "${this::class.java.name}: ${exception.message}")
    } }

    protected fun dispatchStateUi(uiState: S) {
        this.uiState.updateStateUi(uiState = uiState)
    }

    protected fun dispatchStateError(error: Throwable) {
        errorsState.emitError(error)
    }

    protected fun dispatchStateLoading(isShowLoading: Boolean){
        loadingState.updateLoadingState(isShowLoading)
    }
}