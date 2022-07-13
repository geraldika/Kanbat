package com.kanbat.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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