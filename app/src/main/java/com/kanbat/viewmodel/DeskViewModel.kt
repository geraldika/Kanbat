/*
 * Copyright 2022 Yulia Batova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
