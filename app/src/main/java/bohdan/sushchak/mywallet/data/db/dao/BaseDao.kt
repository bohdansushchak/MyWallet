package bohdan.sushchak.mywallet.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {

    @Insert
    fun insert(obj: T) : Long

    @Insert
    fun insert(obj: List<T>) : List<Long>

    @Update
    fun update(obj: T) : Int

    @Delete
    fun delete(obj: T) : Int
}