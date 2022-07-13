package com.kanbat.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Desk(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String
)