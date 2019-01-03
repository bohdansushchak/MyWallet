package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category (
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val title: String,
        val color: String
)
