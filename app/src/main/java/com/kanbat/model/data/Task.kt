package com.kanbat.model.data

import androidx.annotation.ColorRes
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
    @SerializedName("points")
    val points: List<Point>,
    @SerializedName("timeCreatedAt")
    val timeCreatedAt: Long,
    @SerializedName("timeDoneAt")
    val timeDoneAt: Long
)

sealed class TaskState(val state: Int, @ColorRes val color: Int) {
    object Created : TaskState(0, R.color.colorInProgress)
    object Completed : TaskState(1, R.color.colorCompleted)
    object Archived : TaskState(2, R.color.textColorSecondary)

    companion object {
        const val CREATED = 0
        const val COMPLETED = 1
        const val ARCHIVED = 2

        fun getTaskStateByType(type: Int): TaskState {
            return when (type) {
                CREATED -> Created
                COMPLETED -> Completed
                else -> Archived
            }
        }
    }
}