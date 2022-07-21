package com.kanbat.viewmodel

import androidx.lifecycle.*
import com.kanbat.model.data.Desk
import com.kanbat.model.data.Task
import com.kanbat.model.data.TaskState
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.TaskRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddTaskViewModel(
    private val deskId: Long,
    private val deskRepository: DeskRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    var taskText: String = ""
        set(value) {
            field = value
            validateTask()
        }

    var points = mutableListOf<String>()
        set(value) {
            field = value
            validateTask()
        }

    private val deskFlow: StateFlow<Desk?> = deskRepository
        .getDeskById(deskId)
        .catch { }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val desk get() = deskFlow.filterNotNull()

    private var isEnabledFlow = MutableStateFlow<Boolean>(false).apply { value = false }
    val isEnabled get() = isEnabledFlow

    private var isTaskAddedFlow = MutableStateFlow<Boolean>(false).apply { value = false }
    val isTaskAdded get() = isTaskAddedFlow

    fun onAddTaskClicked() {
        viewModelScope.launch {
            val result = taskRepository.addTask(
                Task(
                    id = 0L,
                    deskId = deskId,
                    title = "",
                    text = taskText,
                    state = TaskState.Created.state,
                    priority = 0,
                    points = emptyList(),
                    timeCreatedAt = System.currentTimeMillis(),
                    timeDoneAt = 0L
                )
            )
            isTaskAddedFlow.value = result > 0
        }
    }

    private fun validateTask() {
        isEnabledFlow.value = taskText.isNotEmpty()
    }

    class Factory(
        private val deskId: Long,
        private val deskRepository: DeskRepository,
        private val taskRepository: TaskRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddTaskViewModel(deskId, deskRepository, taskRepository) as T
        }
    }
}
