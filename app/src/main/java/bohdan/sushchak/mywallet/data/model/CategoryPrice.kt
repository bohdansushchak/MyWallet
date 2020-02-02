package bohdan.sushchak.mywallet.data.model

import androidx.room.ColumnInfo

data class CategoryPrice(
    @ColumnInfo(name = "category_title")
    val title: String?,
    @ColumnInfo(name = "color")
    val color: Int?,
    @ColumnInfo(name = "total_price")
    val totalPrice: Double
) {
    override fun toString(): String {
        return "{Title: $title price: $totalPrice}"
    }
}
