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