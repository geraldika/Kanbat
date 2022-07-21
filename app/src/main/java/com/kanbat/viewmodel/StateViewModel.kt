package com.kanbat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.base.CharMatcher.any
import kotlinx.coroutines.flow.*

abstract class StateViewModel<S> : ViewModel() {

//    abstract val _state: Flow<S>
//    private val _viewState = MutableLiveData<S>()
//
//    init {
//        _state
//            .onEach {
//                _viewState.value = it
//            }.launchIn(viewModelScope)
//    }
//
//    val viewState: LiveData<S>
//        get() = _viewState
//
//    protected val currentState: S
//        get() = _state
//
//    protected fun setState(state: S) {
//        _state.value = state
//    }
}