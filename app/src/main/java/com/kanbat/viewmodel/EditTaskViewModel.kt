package com.kanbat.viewmodel

import androidx.lifecycle.*
import com.kanbat.model.data.Desk
import com.kanbat.model.data.Point
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
        set(value) {
            field = value
            validateTask()
        }

    private val deskFlow: StateFlow<Desk?> = deskRepository
        .getDeskById(deskId)
        .catch { }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val desk get() = deskFlow.filterNotNull()

    private val taskFlow: StateFlow<Task?> = taskRepository
        .getTaskById(taskId)
        .catch { }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val task get() = taskFlow.filterNotNull()

    private var isEnabledFlow = MutableStateFlow<Boolean>(false).apply { value = false }
    val isEnabled get() = isEnabledFlow

    private var isTaskEditedFlow = MutableStateFlow<Boolean>(false).apply { value = false }
    val isTaskEdited get() = isTaskEditedFlow

    private var isEditModeFlow = MutableStateFlow<Boolean>(false).apply { value = false }
    val isEditMode get() = isEditModeFlow

    private var isChangeStateModeFlow = MutableStateFlow<Boolean>(false).apply { value = false }
    val isChangeStateMode get() = isChangeStateModeFlow

    fun onEditTaskClicked(points: List<String>) {
        viewModelScope.launch {
            val result = taskRepository.addTask(
                Task(
                    id = taskId,
                    deskId = deskId,
                    title = "",
                    text = taskText,
                    state = TaskState.Created.state,
                    priority = 0,
                    points = points.filter { it.isNotEmpty() }
                        .map { Point(0L, it, System.currentTimeMillis(), 0L) },
                    timeCreatedAt = System.currentTimeMillis(),
                    timeDoneAt = 0L
                )
            )
            isTaskEdited.value = result > 0
        }
    }

    fun onEditModeChanged(isEditMode: Boolean) {
        isEditModeFlow.value = isEditMode
    }

    fun onStateChangeClicked() {
        isChangeStateModeFlow.value = !isChangeStateModeFlow.value
    }

    private fun validateTask() {
        isEnabledFlow.value = taskText.isNotEmpty()
    }

    fun onCompleteTaskClicked() {
        viewModelScope.launch {
            taskRepository.changeTaskState(taskId, TaskState.Completed.state)
        }
    }

    fun onArchiveTaskClicked() {
        viewModelScope.launch {
            taskRepository.changeTaskState(taskId, TaskState.Archived.state)
        }
    }

//    fun onBackAction(): Boolean {
//        return if (isEditMode.value) {
//            onEditModeChanged(false)
//        } else {
//            true
//        }
//    }

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
