package com.hieunt.base.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoadingState {
    private val _loadingState : MutableStateFlow<Boolean> = MutableStateFlow(value = false)

    val loadingState : StateFlow<Boolean>
        get() = _loadingState.asStateFlow()

    fun updateLoadingState(isShowLoading: Boolean) {
        _loadingState.update { isShowLoading }
    }
}