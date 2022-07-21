package com.kanbat.viewmodel

import androidx.lifecycle.*
import com.kanbat.model.data.Desk
import com.kanbat.model.repository.DeskRepository
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val deskRepository: DeskRepository
) : ViewModel() {

    private val desksFlow: StateFlow<List<Desk>?> = deskRepository
        .getAllDesks()
        .catch { }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val desks get() = desksFlow.filterNotNull()

    class Factory(
        private val deskRepository: DeskRepository,

        ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(deskRepository) as T
        }
    }
}