package bohdan.sushchak.mywallet.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot

@Entity(tableName = "categories")
data class CategoryEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryId")
    val categoryId: Long? = null,

    @ColumnInfo(name = "category_title")
    var categoryTitle: String,

    @ColumnInfo(name = "color")
    var color: Int
) {
    companion object {
        val emptyCategoryEntity: CategoryEntity
            get() {
                return CategoryEntity(null, "", 0)
            }

        fun fromDocument(doc: DocumentSnapshot): CategoryEntity {
            val categoryId = doc["id"].toString().toLong()
            val color = doc["color"].toString().toInt()
            val title = doc["title"].toString()

            return CategoryEntity(categoryId = categoryId, color = color, categoryTitle = title)
        }
    }

    override fun toString(): String {
        return "($categoryId, $categoryTitle)"
    }

    override fun equals(other: Any?): Boolean {
        return other is CategoryEntity
                && this.categoryId == other.categoryId
                && this.categoryTitle == other.categoryTitle
                && this.color == other.color
    }
}
