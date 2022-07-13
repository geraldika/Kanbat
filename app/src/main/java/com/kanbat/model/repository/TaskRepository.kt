package com.kanbat.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kanbat.model.data.Task
import com.kanbat.model.local.TaskLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskLocalDataSource: TaskLocalDataSource) {

    suspend fun addTask(task: Task) = taskLocalDataSource.insertTask(task)

    fun getAllTasks(): Flow<List<Task>> = taskLocalDataSource.getAllTasks()

    fun getTasksByDeskId(deskId: Long): Flow<PagingData<Task>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                taskLocalDataSource.getTasksByDeskId(deskId)
            }
        ).flow
    }

    fun getTasks(): Flow<PagingData<Task>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                taskLocalDataSource.getTasks()
            }
        ).flow
    }

    suspend fun deleteTask(task: Task) = taskLocalDataSource.deleteTask(task)

    companion object {
        private const val PAGE_SIZE = 10
    }
}