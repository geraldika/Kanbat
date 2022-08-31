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

package com.kanbat.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.kanbat.model.TaskComposite
import com.kanbat.model.data.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao : BaseDao<Task> {
    @Query("SELECT * FROM Task")
    fun getAllTaskComposites(): PagingSource<Int, TaskComposite>

    @Query("SELECT * FROM Task WHERE deskId =:deskId")
    fun getTaskCompositesByDeskId(deskId: Long): PagingSource<Int, TaskComposite>

    @Query("SELECT * FROM Task WHERE deskId =:deskId AND state =:state")
    fun getTaskCompositesByDeskIdAndState(
        deskId: Long,
        state: Int
    ): PagingSource<Int, TaskComposite>

    @Query("SELECT * FROM Task")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE id =:id")
    fun getTaskById(id: Long): Flow<Task>

    @Query("UPDATE Task SET state = :state WHERE id = :taskId")
    suspend fun changeTaskState(taskId: Long, state: Int)

    @Query("SELECT * FROM Task WHERE deskId =:deskId")
    fun getTasksByDeskId(deskId: Long): PagingSource<Int, Task>

    @Query("SELECT * FROM Task")
    fun getTasks(): PagingSource<Int, Task>

    @Query("DELETE FROM Task WHERE id = :id")
    suspend fun deleteTaskById(id: Long): Int
}