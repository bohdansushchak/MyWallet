package bohdan.sushchak.mywallet.data.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "orderId")
    var orderId: Long?,

    @ColumnInfo(name = "title")
    var title: String?,

    @ColumnInfo(name = "date")
    var date: Long,

    @ColumnInfo(name = "total_price")
    var price: Double

) : Parcelable {

    @Ignore
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readLong(),
        parcel.readDouble()
    )

    override fun toString(): String {
        return "(categoryId:$orderId, categoryTitle:$title, date:$date, price:$price)"
    }

    @Ignore
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(orderId)
        parcel.writeString(title)
        parcel.writeLong(date)
        parcel.writeDouble(price)
    }

    @Ignore
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderEntity> {
        @Ignore
        override fun createFromParcel(parcel: Parcel): OrderEntity {
            return OrderEntity(parcel)
        }

        @Ignore
        override fun newArray(size: Int): Array<OrderEntity?> {
            return arrayOfNulls(size)
        }

        @Ignore
        fun fromDocument(doc: DocumentSnapshot): OrderEntity {
            val orderId = doc["id"].toString().toLong()
            val title = doc["title"].toString()
            val date = doc["date"].toString().toLong()
            val price = doc["price"].toString().toDouble()

            return OrderEntity(orderId = orderId, title = title, date = date, price = price)
        }
    }
}