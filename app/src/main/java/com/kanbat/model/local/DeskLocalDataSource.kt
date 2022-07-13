package com.kanbat.model.local

import com.kanbat.dao.KanbatDatabase
import com.kanbat.model.data.Desk
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeskLocalDataSource @Inject constructor(database: KanbatDatabase) {

    private val deskDao = database.deskDao()

    suspend fun insertDesk(desk: Desk) = deskDao.insert(desk)

    fun getAllDesks(): Flow<List<Desk>> = deskDao.getAllDesks()

    fun getDeskById(id: Long) = deskDao.getDeskById(id)

    suspend fun deleteDesk(desk: Desk) = deskDao.delete(desk)

}