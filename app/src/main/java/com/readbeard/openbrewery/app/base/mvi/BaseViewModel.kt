package com.readbeard.openbrewery.app.base.mvi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE, INTENT>(
    initialState: STATE
) : ViewModel() {

    private val _state = mutableStateOf(initialState)
    val state: State<STATE> = _state

    abstract fun onIntent(intent: INTENT)

    protected fun setState(state: STATE) {
        viewModelScope.launch(context = Dispatchers.Main) {
            _state.value = state
        }
    }
}
