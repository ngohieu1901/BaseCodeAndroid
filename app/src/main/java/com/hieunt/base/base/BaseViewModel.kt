package com.hieunt.base.base

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hieunt.base.ui_state.UiStateStore
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<S : Any> : ViewModel() {
    abstract fun initState(): S

    val uiStore by lazy { UiStateStore(this.initState()) }

    val currentState: S
        get() = uiStore.uiState

    private val _errorFlow = MutableSharedFlow<Throwable>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val errorFlow: SharedFlow<Throwable>
        get() = _errorFlow

    private val _loadingState : MutableStateFlow<Boolean> = MutableStateFlow(value = false)

    val loadingState : MutableStateFlow<Boolean>
        get() = _loadingState

    protected val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("coroutineException1901", "${exception.message}")
    }

    protected fun dispatchError(error: Throwable) {
        _errorFlow.tryEmit(error)
    }

    protected fun dispatchStateUi(uiState: S) {
        uiStore.dispatchStateUi(uiState = uiState)
    }

    protected fun updateStateUi(uiState: S) {
        uiStore.updateStateUi(uiState = uiState)
    }

    protected fun dispatchStateLoading(isShowLoading: Boolean){
        _loadingState.value = isShowLoading
    }

    protected fun updateStateLoading(isShowLoading: Boolean){
        _loadingState.update { isShowLoading }
    }
}