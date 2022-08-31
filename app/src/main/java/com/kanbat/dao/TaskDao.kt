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