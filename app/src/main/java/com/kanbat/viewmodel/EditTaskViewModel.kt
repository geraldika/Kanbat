package com.kanbat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kanbat.model.data.Desk
import com.kanbat.model.data.Task
import com.kanbat.model.data.TaskState
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EditTaskViewModel(
    private val deskId: Long,
    private val taskId: Long,
    private val deskRepository: DeskRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    var taskText: String = ""

    private val deskState: StateFlow<Desk?> = deskRepository
        .getDeskById(deskId)
        .catch { }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val deskUiState get() = deskState.filterNotNull()

    private val taskState: StateFlow<Task?> = taskRepository
        .getTaskById(taskId)
        .catch { }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val taskUiState
        get() = taskState
            .filterNotNull()
            .map { task ->
                val state = TaskState.getTaskStateByType(task.state)
                val isVisible = isTaskMenuVisibleState.value.second
                isTaskMenuVisibleState.value = Pair(state, isVisible)
                task
            }

    private var isOnBackPressedState = MutableStateFlow(false)
    val isOnBackPressedUiState
        get() = isOnBackPressedState
            .filter { it }

    private var isEditModeState = MutableStateFlow(false)
    val isEditModeUiState
        get() = isEditModeState

    private var isTaskMenuVisibleState: MutableStateFlow<Pair<TaskState, Boolean>> =
        MutableStateFlow(Pair(TaskState.InProgress, false))
    val isTaskMenuVisibleUiState get() = isTaskMenuVisibleState

    fun onEditModeChanged() {
        if (isEditModeState.value && taskText.isNotEmpty()) {
            viewModelScope.launch {
                isEditModeState.value = taskRepository.addTask(
                    Task(
                        id = taskId,
                        deskId = deskId,
                        title = "",
                        text = taskText,
                        state = TaskState.InProgress.state,
                        priority = 0,
                        timeCreatedAt = System.currentTimeMillis(),
                        timeDoneAt = 0L
                    )
                ) == 0L
            }
        } else {
            isEditModeState.value = true
        }
    }

    fun onTaskMenuClicked() {
        val state = TaskState
            .getTaskStateByType(taskState.value?.state ?: TaskState.InProgress.state)
        val isVisible = !isTaskMenuVisibleState.value.second
        isTaskMenuVisibleState.value = Pair(state, isVisible)
    }

    fun onChangeTaskStateClicked(taskState: TaskState) {
        viewModelScope.launch {
            taskRepository.changeTaskState(taskId, taskState.state)
        }
    }

    fun onDeleteTaskClicked() {
        viewModelScope.launch {
            isOnBackPressedState.value = taskRepository.deleteTask(taskId)
        }
    }

    fun onBackAction() {
        if (isEditModeState.value) {
            isEditModeState.value = false
        } else {
            isOnBackPressedState.value = true
        }
    }

    class Factory(
        private val deskId: Long,
        private val taskId: Long,
        private val deskRepository: DeskRepository,
        private val taskRepository: TaskRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditTaskViewModel(deskId, taskId, deskRepository, taskRepository) as T
        }
    }
}
