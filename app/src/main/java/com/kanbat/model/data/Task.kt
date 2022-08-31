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
    @SerializedName("timeCreatedAt")
    val timeCreatedAt: Long,
    @SerializedName("timeDoneAt")
    val timeDoneAt: Long
)

sealed class TaskState(val state: Int, @ColorRes val color: Int) {
    object InProgress : TaskState(0, R.color.colorInProgress)
    object Completed : TaskState(1, R.color.colorCompleted)
    object Archived : TaskState(2, R.color.colorArchived)

    companion object {
        private const val IN_PROGRESS = 0
        private const val COMPLETED = 1
        const val ARCHIVED = 2

        fun getTaskStateByType(type: Int): TaskState {
            return when (type) {
                IN_PROGRESS -> InProgress
                COMPLETED -> Completed
                else -> Archived
            }
        }
    }
}

//@Entity
//data class MessageAndFulldiveUserComposite(
//    @SerializedName("message")
//    @Embedded
//    val message: ChatMessage,
//    @SerializedName("chatFulldiveUser")
//    @Embedded
//    val fulldiveUser: FulldiveUser?
//)

//MessageAndFulldiveUserComposite