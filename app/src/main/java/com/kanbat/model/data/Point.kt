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

package com.kanbat.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Point")
data class Point(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long,
    @SerializedName("taskId")
    val taskId: Long,
    @SerializedName("text")
    val text: String,
    @SerializedName("timeCreatedAt")
    val timeCreatedAt: Long,
    @SerializedName("timeDoneAt")
    val timeDoneAt: Long
) {
    companion object {
        fun generatePoint(text: String, taskId: Long) =
            Point(0L, taskId, text, System.currentTimeMillis(), 0L)
    }
}

val EMPTY_POINT = Point(0L, 0L, "", System.currentTimeMillis(), 0L)

fun Point.isDone() = timeDoneAt > 0L