package bohdan.sushchak.mywallet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bohdan.sushchak.mywallet.data.db.entity.MetaDataEntity

@Dao
interface MetaDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(metaDataEntity: MetaDataEntity)

    @Query("select * from meta_data")
    fun getMetaData(): MetaDataEntity?
}