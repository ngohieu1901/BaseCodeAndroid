package com.hieunt.base.presentations.feature.container

import com.hieunt.base.base.BaseMvvmViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContainerViewModel @Inject constructor(): BaseMvvmViewModel<ContainerUiState>() {
    override fun initState(): ContainerUiState {
        return ContainerUiState(state = "")
    }

}