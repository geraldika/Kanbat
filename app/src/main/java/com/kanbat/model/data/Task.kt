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

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.samples.gridtopager.R

@Entity(tableName = "Task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long,
    @SerializedName("deskId")
    val deskId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("state")
    val state: Int,
    @SerializedName("priority")
    val priority: Int,
    @SerializedName("timeCreatedAt")
    val timeCreatedAt: Long,
    @SerializedName("timeDoneAt")
    val timeDoneAt: Long
)

sealed class TaskState(val state: Int, @ColorRes val color: Int, @StringRes val title: Int) {
    object InProgress : TaskState(0, R.color.colorInProgress, R.string.str_filter_tasks_in_progress)
    object Completed : TaskState(1, R.color.colorCompleted, R.string.str_filter_tasks_completed)
    object Archived : TaskState(2, R.color.colorArchived, R.string.str_filter_tasks_in_archived)
    object None : TaskState(0, 0, 0)

    companion object {
        private const val IN_PROGRESS = 0
        private const val COMPLETED = 1
        const val ARCHIVED = 2

        fun getTaskStateByType(type: Int): TaskState {
            return when (type) {
                IN_PROGRESS -> InProgress
                COMPLETED -> Completed
                ARCHIVED -> Archived
                else -> None
            }
        }
    }
}