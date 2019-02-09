package bohdan.sushchak.mywallet.data.db.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        override var id: Long?,

        @ColumnInfo(name = "title")
        var title: String,

        @ColumnInfo(name = "date")
        var date: Long,

        @ColumnInfo(name = "total_price")
        var price: Double

) : BaseEntity(), Parcelable {

    @Ignore
    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readLong(),
            parcel.readDouble())

    override fun toString(): String {
        return "(id:$id, title:$title, date:$date, price:$price)"
    }

    @Ignore
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
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
    }
/*
    override fun equals(other: Any?): Boolean {
        return if (other is OrderEntity) {
            this.id == other.id
                    && this.date == other.date
                    && this.price == other.price
                    && this.title == other.title
        } else false
    }
    */
}