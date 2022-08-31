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