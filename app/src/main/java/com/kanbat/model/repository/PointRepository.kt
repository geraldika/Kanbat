/*
 * Copyright 2022 Yulia Batova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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