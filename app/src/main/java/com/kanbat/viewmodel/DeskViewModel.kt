package com.kanbat.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.kanbat.model.data.Task
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class DeskViewModel(
    private val deskId: Long,
    private val deskRepository: DeskRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var deskFlow = deskRepository.getDeskById(deskId)
    val desk get() = deskFlow
    private var tasksFlow = taskRepository.getTasksByDeskId(deskId).cachedIn(viewModelScope)
    val tasks get() = tasksFlow
    private var selectedTaskFlow = MutableStateFlow<Task?>(value = null)
    val selectedTask get() = selectedTaskFlow.filterNotNull()

    fun onTaskClicked(task: Task) {
        selectedTaskFlow.value = task
    }

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
