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

package com.kanbat.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kanbat.model.data.Desk
import com.kanbat.model.data.Point
import com.kanbat.model.data.Task

@Database(
    entities = [
        Desk::class,
        Task::class,
        Point::class
    ],
    version = 1,
    exportSchema = false
)

abstract class KanbatDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "kanbat_db"
    }

    abstract fun deskDao(): DeskDao
    abstract fun taskDao(): TaskDao
    abstract fun pointDao(): PointDao
}
