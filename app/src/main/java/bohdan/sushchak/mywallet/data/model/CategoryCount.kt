package bohdan.sushchak.mywallet.data.model

import androidx.room.ColumnInfo

data class CategoryCount (
    @ColumnInfo(name = "category_id")
    var categoryId: Long?,

    @ColumnInfo(name = "count")
    var count: Int
)
{
    override fun toString(): String {
        return "CategoryId: $categoryId | count: $count"
    }
}