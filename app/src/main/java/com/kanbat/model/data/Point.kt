package com.kanbat.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Point(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long,
    val text: String,
    val timeCreatedAt: Long,
    val timeDoneAt: Long
)

fun Point.isDone() = timeDoneAt > 0L