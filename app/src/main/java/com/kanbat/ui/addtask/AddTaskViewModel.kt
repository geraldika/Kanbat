package com.kanbat.ui.addtask

import androidx.lifecycle.*
import com.kanbat.model.data.Task
import com.kanbat.model.repository.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val taskRepository: TaskRepository) :
    ViewModel() {

    class Factory(private val taskRepository: TaskRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddTaskViewModel(taskRepository) as T
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskRepository.addTask(task)
        }
    }
}
