package com.kanbat.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.kanbat.model.data.Desk
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class DeskViewModel(
    private val deskId: Long,
    private val deskRepository: DeskRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var deskFlow = deskRepository.getDeskById(deskId)
    val desk get() = deskFlow
    private var tasksFlow = taskRepository.getTasksByDeskId(deskId).cachedIn(viewModelScope)
    val tasks get() = tasksFlow

    class Factory(
        private val deskId: Long,
        private val deskRepository: DeskRepository,
        private val taskRepository: TaskRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DeskViewModel(deskId, deskRepository, taskRepository) as T
        }
    }
}
