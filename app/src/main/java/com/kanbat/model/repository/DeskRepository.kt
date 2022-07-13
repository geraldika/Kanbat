package com.kanbat.model.repository

import com.kanbat.model.data.Desk
import com.kanbat.model.local.DeskLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeskRepository @Inject constructor(private val deskLocalDataSource: DeskLocalDataSource) {

    suspend fun insertTask(desk: Desk) = deskLocalDataSource.insertDesk(desk)

    fun getAllDesks(): Flow<List<Desk>> = deskLocalDataSource.getAllDesks()

    fun getDeskById(id: Long) = deskLocalDataSource.getDeskById(id)

    suspend fun deleteDesk(desk: Desk) = deskLocalDataSource.deleteDesk(desk)
}