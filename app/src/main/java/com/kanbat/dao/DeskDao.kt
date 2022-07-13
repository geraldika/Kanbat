package com.kanbat.dao

import androidx.room.Dao
import androidx.room.Query
import com.kanbat.model.data.Desk
import kotlinx.coroutines.flow.Flow

@Dao
interface DeskDao : BaseDao<Desk> {

    @Query("SELECT * FROM Desk")
    fun getAllDesks(): Flow<List<Desk>>

    @Query("SELECT * FROM Desk WHERE id=:id")
    fun getDeskById(id: Long): Flow<Desk>
}

