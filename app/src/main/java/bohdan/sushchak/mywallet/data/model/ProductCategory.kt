package bohdan.sushchak.mywallet.data.model

import androidx.room.ColumnInfo

data class ProductCategory(
    @ColumnInfo(name = "productId")
    var id: Long?,

    @ColumnInfo(name = "productTitle")
    var productTitle: String,

    @ColumnInfo(name = "price")
    var price: Double,

    @ColumnInfo(name = "category_title")
    var categoryTitle: String
)