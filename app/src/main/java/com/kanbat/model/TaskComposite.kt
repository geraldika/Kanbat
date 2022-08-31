package com.kanbat.model

import androidx.room.Embedded
import androidx.room.Relation
import com.google.gson.annotations.SerializedName
import com.kanbat.model.data.Point
import com.kanbat.model.data.Task

data class TaskComposite(
    @SerializedName("Task")
    @Embedded
    val task: Task,
    @SerializedName("points")
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val points: List<Point>
)