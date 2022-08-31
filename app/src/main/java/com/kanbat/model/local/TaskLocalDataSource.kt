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

package com.kanbat.model.local

import androidx.paging.PagingSource
import com.kanbat.dao.KanbatDatabase
import com.kanbat.model.TaskComposite
import com.kanbat.model.data.Task
import com.kanbat.model.data.TaskState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskLocalDataSource @Inject constructor(private val database: KanbatDatabase) {

    private val dao = database.taskDao()

    fun getAllTaskComposites(): PagingSource<Int, TaskComposite> = dao.getAllTaskComposites()

    fun getTaskCompositesByDeskId(deskId: Long): PagingSource<Int, TaskComposite> = dao
        .getTaskCompositesByDeskId(deskId)

    fun getTaskCompositesByDeskIdAndState(
        deskId: Long,
        state: TaskState
    ): PagingSource<Int, TaskComposite> {
        return dao.getTaskCompositesByDeskIdAndState(deskId, state.state)
    }

    suspend fun insertTask(task: Task) = dao.insert(task)

    suspend fun changeTaskState(taskId: Long, state: Int) = dao.changeTaskState(taskId, state)

    fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    fun getTaskById(id: Long): Flow<Task> = dao.getTaskById(id)

    fun getTasksByDeskId(deskId: Long): PagingSource<Int, Task> = dao.getTasksByDeskId(deskId)

    fun getTasks(): PagingSource<Int, Task> = dao.getTasks()

    suspend fun deleteTask(taskId: Long): Int = dao.deleteTaskById(taskId)

}