package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "date_table", indices = [Index(value = ["date"], unique = true) ])
data class Date(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Long?,

        @ColumnInfo(name = "date")
        val date: Long
)

