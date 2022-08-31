package com.kanbat.model.repository

import com.kanbat.model.data.Desk
import com.kanbat.model.local.DeskLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeskRepository @Inject constructor(private val localDataSource: DeskLocalDataSource) {

    suspend fun insertTask(desk: Desk) = localDataSource.insertDesk(desk)

    fun getAllDesks(): Flow<List<Desk>> = localDataSource.getAllDesks()

    fun getDeskById(id: Long): Flow<Desk> = localDataSource.getDeskById(id)

    suspend fun deleteDesk(desk: Desk) = localDataSource.deleteDesk(desk)
}