package com.kanbat.model.repository

import com.kanbat.model.data.Point
import com.kanbat.model.local.PointLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PointRepository @Inject constructor(private val localDataSource: PointLocalDataSource) {

    fun getPointsTaskById(taskId: Long): Flow<List<Point>> =
        localDataSource.getPointsTaskById(taskId)

    suspend fun insertOrUpdatePoint(point: Point) = localDataSource.insertOrUpdatePoint(point)

    suspend fun insertPoints(points: List<Point>) = localDataSource.insertPoints(points)

    suspend fun deletePoint(point: Point) = localDataSource.deletePoint(point)

}