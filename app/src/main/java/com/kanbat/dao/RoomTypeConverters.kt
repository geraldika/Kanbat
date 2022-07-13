package com.kanbat.dao

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import com.kanbat.model.data.Point

@Suppress("unused")
class RoomTypeConverters {
    private val gson = GsonBuilder()
        .create()

    @TypeConverter
    fun stringToPoints(data: String?): List<Point>? {
        return gson.fromJson(data, object : TypeToken<List<Point>>() {}.type)
    }

    @TypeConverter
    fun userPointsToString(obj: List<Point>?): String? = gson.toJson(obj)
}
