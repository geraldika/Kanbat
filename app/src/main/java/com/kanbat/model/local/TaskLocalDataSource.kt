package com.kanbat.model.local

import androidx.paging.PagingSource
import com.kanbat.dao.KanbatDatabase
import com.kanbat.model.data.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskLocalDataSource @Inject constructor(private val database: KanbatDatabase) {

    private val taskDao = database.taskDao()

    suspend fun insertTask(task: Task) = taskDao.insert(task)

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    fun getTasksByDeskId(deskId: Long): PagingSource<Int, Task> = taskDao.getTasksByDeskId(deskId)

    fun getTasks(): PagingSource<Int, Task> = taskDao.getTasks()

    suspend fun deleteTask(task: Task) = taskDao.delete(task)

}