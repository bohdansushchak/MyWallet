package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        override val id: Long? = null,

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "color")
        var color: Int

): BaseEntity() {
    companion object {
        val emptyCategoryEntity: CategoryEntity
            get() {
                return CategoryEntity(null, "", 0)
            }
    }

    override fun toString(): String {
        return "(id:$id, title:$title, $color$color)"
    }

    override fun equals(other: Any?): Boolean {
        return other is CategoryEntity
                && this.id == other.id
                && this.title == other.title
                && this.color == other.color
    }
}