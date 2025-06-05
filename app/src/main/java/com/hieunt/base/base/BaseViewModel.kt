package com.hieunt.base.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hieunt.base.ui_state.UiStateStore
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<S : Any> : ViewModel() {
    abstract fun initState(): S

    val uiStore by lazy { UiStateStore(this.initState()) }

    val currentState: S
        get() = uiStore.uiState

    private val _errorLiveEvent: MutableLiveData<Throwable> = MutableLiveData<Throwable>()

    val errorLiveEvent: LiveData<Throwable>
        get() = _errorLiveEvent

    private val _loadingState : MutableStateFlow<Boolean> = MutableStateFlow(value = false)

    val loadingState : MutableStateFlow<Boolean>
        get() = _loadingState

    protected val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("coroutineException1901", "${exception.message}")
    }

    protected fun dispatchStateUi(uiState: S) {
        uiStore.dispatchStateUi(uiState = uiState)
    }

    protected fun updateStateUi(uiState: S) {
        uiStore.updateStateUi(uiState = uiState)
    }

    protected fun dispatchError(error: Throwable) {
        _errorLiveEvent.postValue(error)
    }

    protected fun dispatchStateLoading(isShowLoading: Boolean){
        _loadingState.value = isShowLoading
    }
}