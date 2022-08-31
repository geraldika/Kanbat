package com.kanbat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kanbat.model.data.Desk
import com.kanbat.model.repository.DeskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val deskRepository: DeskRepository
) : ViewModel() {

    private val desksState: StateFlow<List<Desk>?> = deskRepository
        .getAllDesks()
        .catch { }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val desksUiState get() = desksState.filterNotNull()

    fun onDeleteDeskClicked(desk: Desk) {
        viewModelScope.launch {
            deskRepository.deleteDesk(desk)
        }
    }

    class Factory(
        private val deskRepository: DeskRepository,

        ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(deskRepository) as T
        }
    }
}