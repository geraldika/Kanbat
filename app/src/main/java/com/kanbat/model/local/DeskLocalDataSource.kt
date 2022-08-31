package com.kanbat.model.local

import com.kanbat.dao.KanbatDatabase
import com.kanbat.model.data.Desk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeskLocalDataSource @Inject constructor(database: KanbatDatabase) {

    private val dao = database.deskDao()

    suspend fun insertDesk(desk: Desk) = dao.insert(desk)

    fun getAllDesks(): Flow<List<Desk>> = dao.getAllDesks()

    fun getDeskById(id: Long): Flow<Desk> = dao.getDeskById(id)

    suspend fun deleteDesk(desk: Desk) = dao.delete(desk)

}