package com.hieunt.base.ui_state

import android.util.Log
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UiStateStore<T : Any>(
    initialState: T
) {
    private val _uiState = MutableStateFlow(initialState)

    val uiState: T get() = _uiState.asStateFlow().value

    suspend fun collect(collector: FlowCollector<T>) {
        Log.e("UiStateStore", "collectData: ${_uiState.value} ", )
        _uiState.collect(collector)
    }

    fun dispatchStateUi(uiState: T){
        Log.e("UiStateStore", "dispatchStateUi: ${_uiState.value}", )
        _uiState.value = uiState
    }
}