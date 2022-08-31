package com.kanbat.model

import com.google.gson.annotations.SerializedName
import com.kanbat.model.data.Point

data class PointItem(
    @SerializedName("point")
    val point: Point,
    @SerializedName("isEditMode")
    val isEditMode: Boolean
)