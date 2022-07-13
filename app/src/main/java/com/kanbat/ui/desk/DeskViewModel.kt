package com.kanbat.ui.desk

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.TaskRepository

class DeskViewModel(
    private val deskId: Long,
    private val deskRepository: DeskRepository,
    private val taskRepository: TaskRepository
) :
    ViewModel() {

    class Factory(
        private val deskId: Long,
        private val deskRepository: DeskRepository,
        private val taskRepository: TaskRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DeskViewModel(deskId, deskRepository, taskRepository) as T
        }
    }

    fun getDesk() = deskRepository.getDeskById(deskId)

    fun getTasks() = taskRepository.getTasksByDeskId(deskId).cachedIn(viewModelScope)
}
