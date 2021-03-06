package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val META_DATA_ID = 1

@Entity(tableName = "meta_data")
data class MetaDataEntity(
    @ColumnInfo(name = "databaseVersion")
    var databaseVersion: Long?,

    @ColumnInfo(name = "userFirebaseId")
    val userFirebaseId: String?
) {
    @PrimaryKey
    var id: Int = META_DATA_ID
}