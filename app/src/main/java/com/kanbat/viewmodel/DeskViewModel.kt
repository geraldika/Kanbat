package com.kanbat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kanbat.model.TaskComposite
import com.kanbat.model.data.Task
import com.kanbat.model.data.TaskState
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class DeskViewModel(
    private val deskId: Long,
    private val deskRepository: DeskRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var deskState = deskRepository.getDeskById(deskId).filterNotNull()
    val deckUiState get() = deskState

    private var taskItemsState = taskRepository.getTaskCompositesByDeskId(deskId)
        .cachedIn(viewModelScope)
        .filterNotNull()
    val taskItemsUiState get() = taskItemsState

    private var selectedTaskState = MutableStateFlow<Task?>(null)
    val selectedTaskUiState get() = selectedTaskState.filterNotNull()

    fun onTaskClicked(item: TaskComposite) {
        selectedTaskState.value = item.task
    }

    fun onFilterTasksClicked(taskState: TaskState) {
     //   getTaskCompositesByDeskIdAndState
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
