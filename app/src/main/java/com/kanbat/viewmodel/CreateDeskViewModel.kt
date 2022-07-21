package com.kanbat.viewmodel

import androidx.lifecycle.*
import com.kanbat.model.data.Desk
import com.kanbat.model.repository.DeskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateDeskViewModel(private val deskRepository: DeskRepository) :
    ViewModel() {

    var deskTitle: String = ""
        set(value) {
            field = value
            validateDesk()
        }

    private var isEnabledFlow = MutableStateFlow<Boolean>(false).apply { value = false }
    val isEnabled get() = isEnabledFlow

    private var isDeckCreatedFlow = MutableStateFlow<Boolean>(false).apply { value = false }
    val isDeskCreated get() = isDeckCreatedFlow

    fun onCreateDeskClicked() {
        viewModelScope.launch {
            isDeckCreatedFlow.value = deskRepository.insertTask(Desk(0L, deskTitle)) > 0
        }
    }

    private fun validateDesk() {
        isEnabledFlow.value = deskTitle.isNotEmpty()
    }

    class Factory(
        private val deskRepository: DeskRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateDeskViewModel(deskRepository) as T
        }
    }
}