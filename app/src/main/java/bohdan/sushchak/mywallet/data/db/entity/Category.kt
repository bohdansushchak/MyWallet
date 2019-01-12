package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        override val id: Long? = null,

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "color")
        var color: Int

): BaseEntity() {
    companion object {
        val emptyCategory: Category
            get() {
                return Category(null, "", 0)
            }
    }

    override fun toString(): String {
        return title
    }

    override fun equals(other: Any?): Boolean {
        return other is Category
                && this.id == other.id
                && this.title == other.title
                && this.color == other.color
    }
}
