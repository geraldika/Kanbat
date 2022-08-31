package com.kanbat.model.local

import com.kanbat.dao.KanbatDatabase
import com.kanbat.model.data.Point
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PointLocalDataSource @Inject constructor(database: KanbatDatabase) {

    private val dao = database.pointDao()

    fun getPointsTaskById(taskId: Long): Flow<List<Point>> = dao.getPointsTaskById(taskId)

    suspend fun insertPoints(points: List<Point>) = dao.insertPoints(points)

    suspend fun insertOrUpdatePoint(point: Point) = dao.insert(point)

    suspend fun deletePoint(point: Point) = dao.delete(point)

}