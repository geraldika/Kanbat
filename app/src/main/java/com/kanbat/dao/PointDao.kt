package com.kanbat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kanbat.model.data.Point
import kotlinx.coroutines.flow.Flow

@Dao
interface PointDao : BaseDao<Point> {

    @Query("SELECT * FROM Point WHERE taskId =:taskId")
    fun getPointsTaskById(taskId: Long): Flow<List<Point>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoints(points: List<Point>)
}