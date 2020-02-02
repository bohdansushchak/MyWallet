package bohdan.sushchak.mywallet.data.model

import androidx.room.ColumnInfo

data class MoneyByDate(
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "totalPrice")
    val totalPrice: Double
) {
    override fun toString(): String {
        return "{date: $date, totalPrice: $totalPrice"
    }
}
