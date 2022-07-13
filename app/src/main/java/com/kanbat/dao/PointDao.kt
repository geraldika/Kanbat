package com.kanbat.dao

import androidx.room.Dao
import com.kanbat.model.data.Point

@Dao
interface PointDao : BaseDao<Point> {

}