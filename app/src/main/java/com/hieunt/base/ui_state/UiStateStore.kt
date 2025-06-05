package com.hieunt.base.ui_state

import android.util.Log
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

class UiStateStore<T : Any>(
    initialState: T
) {
    private val _uiState = MutableStateFlow(initialState)

    val uiState: T get() = _uiState.value

    suspend fun collect(collector: FlowCollector<T>) {
        Log.e("UiStateStore", "collectData: ${_uiState.value} ", )
        _uiState.collect(collector)
    }

    suspend fun collectLatest(action: suspend (uiState: T) -> Unit) {
        Log.e("UiStateStore", "collectData: ${_uiState.value} ", )
        _uiState.collectLatest(action)
    }

    fun dispatchStateUi(uiState: T) {
        Log.e("UiStateStore", "dispatchStateUi: ${_uiState.value}", )
        _uiState.value = uiState
    }

    fun updateStateUi(uiState: T) {
        Log.e("UiStateStore", "updateStateUi: ${_uiState.value}", )
        _uiState.update { uiState }
    }
}