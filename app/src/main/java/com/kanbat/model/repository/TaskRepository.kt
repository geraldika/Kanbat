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

package com.kanbat.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kanbat.model.TaskComposite
import com.kanbat.model.data.Task
import com.kanbat.model.data.TaskState
import com.kanbat.model.local.TaskLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val localDataSource: TaskLocalDataSource
) {

    suspend fun addTask(task: Task): Long = localDataSource.insertTask(task)

    suspend fun changeTaskState(taskId: Long, state: Int) = localDataSource
        .changeTaskState(taskId, state)

    fun getAllTasks(): Flow<List<Task>> = localDataSource.getAllTasks()

    fun getTaskById(id: Long): Flow<Task> = localDataSource.getTaskById(id)

    fun getTaskCompositesByDeskId(deskId: Long): Flow<PagingData<TaskComposite>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                localDataSource.getTaskCompositesByDeskId(deskId)
            }
        ).flow
    }

    fun getTaskCompositesByDeskIdAndState(
        deskId: Long,
        state: TaskState
    ): Flow<PagingData<TaskComposite>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                localDataSource.getTaskCompositesByDeskIdAndState(deskId, state)
            }
        ).flow
    }

    fun getAllTaskComposites(): Flow<PagingData<TaskComposite>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                localDataSource.getAllTaskComposites()
            }
        ).flow
    }

    suspend fun deleteTask(taskId: Long): Boolean = localDataSource.deleteTask(taskId) > 0

    companion object {
        private const val PAGE_SIZE = 10
    }
}