package com.kanbat.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kanbat.model.data.Desk
import com.kanbat.model.data.Point
import com.kanbat.model.data.Task

@Database(
    entities = [
        Desk::class,
        Task::class,
        Point::class
    ],
    version = 1,
    exportSchema = true
)

@TypeConverters(RoomTypeConverters::class)
abstract class KanbatDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "kanbat_db"
    }

    abstract fun deskDao(): DeskDao
    abstract fun taskDao(): TaskDao
    abstract fun pointDao(): PointDao
}
