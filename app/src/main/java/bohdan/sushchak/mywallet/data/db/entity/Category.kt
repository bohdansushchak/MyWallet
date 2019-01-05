package bohdan.sushchak.mywallet.data.db.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category (

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name ="id")
        var id: Int? = null,

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "color")
        var color: Int
)
